package gsd.fscat.c

import scala.xml._
import scala.collection._


/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Passos
 * Date: 21/08/14
 * Time: 6:35 PM
 * To change this template use File | Settings | File Templates.
 */
object CXMLTreeWithTagIdAttr {

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  def closingBlockAttrName =  "closingBlock"

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  def idAttrName = "id"

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  def apply(src:Elem) : (Elem, mutable.Seq[Node]) = {

    val flatTree = mutable.ListBuffer[Node]()
    val stack = mutable.Stack[(Elem, Elem)]()

    val newSrc = addIdAttr(src, 0, flatTree, stack)

    if (! stack.isEmpty)
      throw new Exception("Incorrect number of endifs")

    return (newSrc, flatTree)
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  private def addIdAttr(elem:Elem, id:Int, flatTree:mutable.ListBuffer[Node], stack:mutable.Stack[(Elem, Elem)]) : Elem = {

    var newElem = elem % Attribute(None, idAttrName, Text(id.toString), Null)

    newElem match {
      case ifAnnotation if ifAnnotation.namespace == CXMLTree.cppNamespace &&
        (Set("if", "ifdef", "ifndef") contains ifAnnotation.label) => stack.push((newElem, newElem))

      case elseAnnotation if elseAnnotation.namespace == CXMLTree.cppNamespace &&
        (Set("elif", "else") contains elseAnnotation.label) =>

        newElem =
          newElem % Attribute(None, closingBlockAttrName,
            Text(handleElseAnnotation(newElem, stack)._1.attribute(idAttrName).get.toString), Null)

      case endif if endif.namespace == CXMLTree.cppNamespace && endif.label == "endif" =>
        newElem =
          newElem % Attribute(None, closingBlockAttrName,
            Text(handleEndifAnnotation(newElem, stack)._1.attribute(idAttrName).get.toString), Null)

      case _ => /* Do nothing */
    }

    var flatSubtree = mutable.ListBuffer[Node]()
    var newChildren  = mutable.ListBuffer[Node]()

    for(c <- newElem.child) {

      c match {
        case childElem:Elem => newChildren += addIdAttr(childElem, id + 1 + flatSubtree.size, flatSubtree, stack)
        case _ => newChildren += c ; flatSubtree += c
      }
    }

    newElem = newElem.copy(child = newChildren)

    flatTree.append(newElem)
    flatTree.appendAll(flatSubtree)

    return newElem
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  private def checkStackUnderflow[T](stack:mutable.Stack[T], msg:String = "Empty stack") {
    if (stack.length == 0)
      throw new Exception(msg)
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  private def handleElseAnnotation(elseBranch:Elem, stack:mutable.Stack[(Elem, Elem)]) : (Elem, Elem) = {
    checkStackUnderflow(stack, "Misplaced #elif/else " + getLineStr(elseBranch))

    val (fstBranch, lastBranch) = stack.pop

    lastBranch match {
      case tag if tag.namespace == CXMLTree.cppNamespace &&
        ! (Set("elif", "if", "ifdef", "ifndef") contains tag.label) =>
        throw new RuntimeException("Incorrect preceeding directive: " + lastBranch.label + getLineStr(lastBranch))

      case _ => /* Do nothing */
    }

    stack.push((fstBranch, elseBranch))

    return (fstBranch, lastBranch)
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  private def handleEndifAnnotation(node:Elem, stack:mutable.Stack[(Elem, Elem)]) : (Elem, Elem) = {
    checkStackUnderflow(stack, "Misplaced #endif " + getLineStr(node))

    val (fstBranch, lastBranch) = stack.pop
    if (! (Set("if", "ifdef", "ifndef", "elif", "else") contains lastBranch.label))
      throw new RuntimeException("Incorrect preceeding directive: " + lastBranch.label + getLineStr(lastBranch))

    return (fstBranch, lastBranch)
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  private def getLineStr(node:Elem) : String = node.attribute(CXMLTree.posNamespace, "line") match {
    case Some(line) => "at line " + line.toString
    case _          => ""
  }
}
