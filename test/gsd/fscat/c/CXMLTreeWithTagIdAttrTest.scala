package gsd.fscat.c

import gsd.fscat.c.CXMLTreeWithTagIdAttr._
import org.junit.{Test, Ignore}
import org.junit.Assert._
import gsd.fscat.xml.XMLUtils._
import java.io.File
import scala.xml.{Elem, Node}
import gsd.fscat.test.TestResourcePath
import scala.collection.mutable.ListBuffer

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Passos
 * Date: 21/08/14
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */
class CXMLTreeWithTagIdAttrTest {

  private val resources = new TestResourcePath(this.getClass.getPackage.getName)
  import resources._

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
  /* Test configuration parameters                          */

  val verbose = false

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def addIdTest1 =  addIdAttrTest("addIdTest1")

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def addIdTest2 =  addIdAttrTest("addIdTest2")

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def addIdTest3 =  addIdAttrTest("addIdTest3")

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def addIdTest4 =  {
    var exception : Exception = null
    try {
      addIdAttrTest("addIdTest4")
    } catch {
      case e:Exception => exception = e
    }
    assertTrue(exception != null)
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def addIdTest5 =  {
    var exception : Exception = null
    try {
      addIdAttrTest("addIdTest5")
    } catch {
      case e:Exception => exception = e
    }
    assertTrue(exception != null)
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  def addIdAttrTest(testName:String) : Unit = {

    val (root, listOfNodes) = CXMLTreeWithTagIdAttr(CXMLTree(getResource(testName + File.separator + "input.c")))

    if (verbose) {
      println("Test name: " + testName + "\n")
      println(root)
      println("\n------------------------------------\n")
    }

    assert((ListBuffer() ++ flat(root)) == (ListBuffer() ++ listOfNodes))

    for (elseIfBranch <- root \\ "elif") {

      val i = Integer.parseInt(elseIfBranch.attributes.get(closingBlockAttrName).get.text)
      println(i)

      assertTrue(i < listOfNodes.size)
      assertTrue(listOfNodes(i).label == "ifdef" )
    }

    for (elseBranch <- (root \\ "else")) {
      val i = Integer.parseInt(elseBranch.attributes.get(closingBlockAttrName).get.text)

      assertTrue(i < listOfNodes.size)
      assertTrue(listOfNodes(i).label == "ifdef")
    }
  }
}
