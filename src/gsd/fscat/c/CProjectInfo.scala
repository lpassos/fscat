package gsd.fscat.c

import scala.collection._
import scala.collection.JavaConversions._
import scala.math._

import scala.xml.{Elem, Node, Text}

import java.io.File

import gsd.utils.io.FileUtils
import gsd.fscat.c.CXMLTreeWithTagIdAttr._
import gsd.fscat.xml.XMLUtils
import gsd.utils.numeric.MutableNumber
import scala.Some


/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Passos
 * Date: 20/08/14
 * Time: 7:26 PM
 * To change this template use File | Settings | File Templates.
 */

private case class CInfo(val loc:Int,
                         val sd:Map[String, Int],
                         val td:List[(String, Int, Int)],
                         val bmnd:List[(String, Int, Int)]) {

  def this() { this(0, Map.empty, List.empty, List.empty) }

  def + (info:CInfo) = new CInfo(
    this.loc + info.loc,
    mergeSd(this.sd, info.sd),
    this.td ++ info.td,
    this.bmnd ++ info.bmnd
  )

  private def mergeSd(sd1:Map[String, Int], sd2:Map[String, Int]) : Map[String, Int] = {
    var newSd = sd1
    for(k <- sd2.keySet)
      newSd = newSd + (k -> (newSd.get(k).getOrElse(0) + sd2.get(k).get))
    return newSd
  }
}

class CProjectInfo(dir:File) {

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  private val info  = processProject

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  def loc = info.loc

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  def ftConstants  = info.sd.keySet

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  def sd = info.sd

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  def td = info.td

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  def bmnd = info.bmnd

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  private implicit def mutableNumber2Int(x:MutableNumber) = x.intValue()

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  private def processProject : CInfo = {

    var info = new CInfo()

    for(src <- FileUtils.findFiles(dir, CFileUtils.extensionPredicate)) {
      try {
        println("Checking file " + src.getAbsolutePath)
        info = info + getFileInfo(src)
      } catch {
        case e:Exception =>
          e.printStackTrace()
          println("Ignoring file: " + src.getAbsolutePath)
          println("  error: " + e.getMessage)
      }
    }

    return info
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  private def getFileInfo(f:File) : CInfo = {
    val (src, flatTree) = CXMLTreeWithTagIdAttr(CXMLTree(NormalizedCFile(f)))
    return collectInfo(src, flatTree)
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  private def collectInfo(root:Elem, flatTree:mutable.Seq[Node]) : CInfo =  {

    val loc = getLoc(flatTree)

    replaceFileGuardByEmpty(root, flatTree)

    val sd = mutable.Map[String, Int]()
    val td = mutable.MutableList[(String, Int, Int)]()
    val bmnd = mutable.MutableList[(String, Int, Int)]()

    val i = new MutableNumber(0)

    val openedIfdefs = new MutableNumber(-1)
    val maxOpenedIfdefs = new MutableNumber(0)
    var currentBranch : Node = null

    while (i < flatTree.size) {

      flatTree(i) match {
        case annotation if annotation.namespace == CXMLTree.cppNamespace &&
          (Set("if", "ifdef", "ifndef") contains annotation.label)  =>

          currentBranch = updateBranchInfo(openedIfdefs, 1, maxOpenedIfdefs, currentBranch,
            if (currentBranch != null) currentBranch else annotation, bmnd)
          updateAnnotationInfo(annotation, flatTree, sd, td, i)

        case annotation if annotation.namespace == CXMLTree.cppNamespace &&
          annotation.label == "elif"  =>

          currentBranch = updateBranchInfo(openedIfdefs, 0, maxOpenedIfdefs, currentBranch,
            if (openedIfdefs == 0) annotation else currentBranch, bmnd)
          updateAnnotationInfo(annotation, flatTree, sd, td, i)

        case annotation if annotation.namespace == CXMLTree.cppNamespace &&
          annotation.label == "else"  =>

          currentBranch = updateBranchInfo(openedIfdefs, 0, maxOpenedIfdefs, currentBranch,
            if (openedIfdefs == 0) annotation else currentBranch, bmnd)
          i.setValue(i + 1)


        case annotation if annotation.namespace == CXMLTree.cppNamespace &&
          annotation.label == "endif" =>

          currentBranch = updateBranchInfo(openedIfdefs, -1, maxOpenedIfdefs, currentBranch,
            if (openedIfdefs == 0) null else currentBranch, bmnd)
          i.setValue(i + 1)

        case _ => i.setValue(i + 1)
      }
    }

    return CInfo(loc, sd.toMap, td.toList, bmnd.toList)
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  private def updateBranchInfo(openedIfdefs:MutableNumber,
                        inc:Int,
                        maxOpenedIfdefs:MutableNumber,
                        currentBranch:Node,
                        nextBranch:Node,
                        bmnd:mutable.MutableList[(String, Int, Int)]) : Node = {

    openedIfdefs.setValue(openedIfdefs + inc)

    if ((currentBranch ne null) && (currentBranch ne nextBranch)) {
      /* Save current branch and reset control variables */

      bmnd += ((currentBranch.text.toString,
        XMLUtils.getIntAttr(currentBranch, CXMLTree.posNamespace, "line").getOrElse(-1), maxOpenedIfdefs))
      maxOpenedIfdefs.setValue(0)
    }
    else
      maxOpenedIfdefs.setValue(max(maxOpenedIfdefs, openedIfdefs))


    return nextBranch
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  private def updateAnnotationInfo(annotation:Node,
                               flatTree:mutable.Seq[Node],
                               sd:mutable.Map[String, Int],
                               td:mutable.MutableList[(String, Int, Int)],
                               nbrOfVisitedNodes:MutableNumber) : Unit =  {

    val subtreeSize = XMLUtils.flat(annotation).size
    val tdValue = new MutableNumber(0)

    updateAnnotationInfo(nbrOfVisitedNodes + 1, subtreeSize, flatTree, sd, tdValue)
    nbrOfVisitedNodes.setValue(nbrOfVisitedNodes + subtreeSize)

    /* Avoids counting situations like #if 1, or #if 0 */
    if (tdValue > 0)
      td += ((annotation.text.toString, XMLUtils.getIntAttr(annotation, CXMLTree.posNamespace, "line") getOrElse -1,
        tdValue))
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  private def updateAnnotationInfo(start:Int,
                                   length:Int,
                                   flatTree:Seq[Node],
                                   sd:mutable.Map[String, Int],
                                   tdValue:MutableNumber) : Unit = {
    var i = start
    var ftConstants = Set[String]()

    def findNextName(node:Node) : Option[String] = {
      var j = i + 1
      while(j < start + length) {
        flatTree(j) match {
          case <name>{Text(name)}</name> => return Some(name)
          case _ => /* Do nothing */
        }
        j = j + 1 ;
      }
      return None
    }

    while (i < start + length) {
      flatTree(i) match {

        case node if node.label == "call" =>
          /* Moves i to the point where an argument list is found */
          i = i + 1
          while(flatTree(i).label != "argument_list") i = i + 1

        case node if node.label == "name" =>

          val ft = node.text.toString.trim

          if (ft == "defined") {
            findNextName(node) match {
              case Some(_) => /* defined is to be ignored */
              case _    =>
                /* defined is not followed by a name. Consider it as a symbol */
                ftConstants +=  ft
            }
          }
          else {
            ftConstants += ft
          }

        case _ => /* Do nothing */
      }
      i = i + 1
    }

    tdValue.setValue(ftConstants.size)

    for(ft <- ftConstants)
      sd += (ft -> (sd.get(ft).getOrElse(0) + 1))
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  private def getLoc(nodes:Seq[Node]) : Int = {
    for(n <- nodes.reverse if n.isInstanceOf[Elem]) {
      XMLUtils.getIntAttr(n, CXMLTree.posNamespace, "line") match {
        case Some(line) => return line
        case _          => /* Do nothing */
      }
    }
    /* No loc information */
    return 0 ;
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  private def replaceFileGuardByEmpty(root:Elem, nodes:mutable.Seq[Node]) = nodes.tail.reverse.find(
    n => n.namespace == CXMLTree.cppNamespace && n.label == "endif"
  ) match {
    case Some(endif) =>

      val ifdefPos = Integer.parseInt(endif.attribute(closingBlockAttrName).get.toString)
      val endifPos = Integer.parseInt(endif.attribute(idAttrName).get.toString)

      val endifSubtreeFlat = XMLUtils.flat(endif)

      if (isFileGuard(root, ifdefPos, endifPos + endifSubtreeFlat.size + 1, nodes)) {

        for(i <- ifdefPos to (XMLUtils.flat(nodes(ifdefPos)).size + ifdefPos)) {
          nodes(i) = <empty/>
        }

        for(i <- endifPos to (endifSubtreeFlat.size + endifPos)) {
          nodes(i) = <empty/>
        }
      }

    case _ => /* Do nothing */
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  private def isFileGuard(root:Elem, ifDefPos:Int, pastEndifPos:Int, nodes:Seq[Node]) : Boolean = {

    /* Note: file guards are necessarily direct descendents of the root element */

    val map = mutable.Map[String,Int]()
    updateAnnotationInfo(ifDefPos, XMLUtils.flat(nodes(ifDefPos)).length, nodes, map, new MutableNumber(0))

    if(map.keySet.size > 1)
      return false

    val elseBranches =  ((root \ "elif") ++ (root \ "else")) filter (n => n.namespace == CXMLTree.cppNamespace &&
      n.attribute(closingBlockAttrName).get.toString == ifDefPos.toString)

    /* If the ifdef has associated branches, it is not a file guard inclusion */
    if (elseBranches.size > 0)
      return false

    /* If there is some content prior to the first ifdef (note: the file was normalized prior to parsing), then
     * the ifdef is not a guard
     */
    for(i <- 1 to ifDefPos - 1) {
      if (nodes(i).text.trim.length > 0)
        return false
    }

    /* If there is some content after the matching endif (note: the file was normalized prior to parsing), then
     * the ifdef is not a guard
     */
    for(i <- pastEndifPos to nodes.size - 1) {
      if (nodes(i).text.trim.length > 0)
        return false
    }

    return true
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  override def toString : String = {
    var buffer = new StringBuffer
    buffer.append("CProjectInfo {\n")
    buffer.append("   dir = " + dir + "\n")
    buffer.append("   loc = " + loc + "\n")
    buffer.append("   ftConstants = " + ftConstants + "\n")
    buffer.append("   sd = " + sd + "\n")
    buffer.append("   td = " + td + "\n")
    buffer.append("   bmnd = " + bmnd + "\n")
    return buffer.toString
  }
}


