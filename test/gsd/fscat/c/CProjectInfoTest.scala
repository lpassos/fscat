package gsd.fscat.c

import gsd.fscat.test.TestResourcePath
import org.junit.Assert._
import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Passos
 * Date: 23/08/14
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
class CProjectInfoTest {

  private val resources = new TestResourcePath(this.getClass.getPackage.getName)
  import resources._

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
  /* Test configuration parameters                          */

  val verbose = true

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def  projectInfoTest1 = projectInfoTest("projectInfoTest1", 4,
    Map("FOO" -> 1),
    List(1),
    List(0)
  )

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def  projectInfoTest2 = projectInfoTest("projectInfoTest2", 4,
    Map("FOO" -> 1),
    List(1),
    List(0)
  )

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def  projectInfoTest3 = projectInfoTest("projectInfoTest3", 3,
    Map("FOO" -> 1, "BAR" -> 1),
    List(2),
    List(0)
  )

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def  projectInfoTest4 = projectInfoTest("projectInfoTest4", 3,
    Map("FOO" -> 1, "Z" -> 1),
    List(2),
    List(0)
  )

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def  projectInfoTest5 = projectInfoTest("projectInfoTest5", 5,
    Map("FOO" -> 1, "Z" -> 1, "BAR" -> 1),
    List(2, 1),
    List(0, 0)
  )

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def  projectInfoTest6 = projectInfoTest("projectInfoTest6", 8,
    Map("FOO" -> 1, "Z" -> 1, "BAR" -> 1),
    List(2, 1),
    List(0, 0)
  )

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def  projectInfoTest7 = projectInfoTest("projectInfoTest7", 8,
    Map("FOO" -> 1, "Z" -> 1, "BAR" -> 1),
    List(2, 1) ,
    List(0, 0)
  )

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  def  projectInfoTest8 = projectInfoTest("projectInfoTest8", 0, Map.empty, List.empty, List.empty)

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def  projectInfoTest9 = projectInfoTest("projectInfoTest9", 15,
    Map("FOO" -> 2, "BAR" -> 1, "Z" -> 1),
    List(1, 2, 1),
    List.fill(4)(0)
  )

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def  projectInfoTest10 = projectInfoTest("projectInfoTest10", 268,
    Map("CONFIG_COMPAT" -> 2),
    List(1, 1),
    List(0, 0)
  )

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def  projectInfoTest11 = projectInfoTest("projectInfoTest11", 4382,
    Map("LIBXML_XPATH_ENABLED" -> 1,
      "DBL_EPSILON" -> 1,
      "HAVE_FLOAT_H" -> 1,
      "DEBUG" -> 1,
      "LIBXML_SCHEMAS_ENABLED" -> 1,
      "HAVE_MATH_H" -> 1,
      "DBL_DIG" -> 1),
    List.fill(7)(1),
    List(2)
  )

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def  projectInfoTest12 = projectInfoTest("projectInfoTest12", 4,
    Map("__GNUC__" -> 1),
    List(1),
    List(0)
  )

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def  projectInfoTest13 = projectInfoTest("projectInfoTest13", 4,
    Map("__GNUC__" -> 1),
    List(1),
    List(0)
  )

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def  projectInfoTest14 = projectInfoTest("projectInfoTest14", 18,
    Map
      ("LEVEL1"   -> 1,
        "LEVEL2_0" -> 1,
        "LEVEL2_1" -> 1,
        "LEVEL2_2" -> 1,
        "LEVEL3_0" -> 1,
        "LEVEL3_1" -> 1,
        "LEVEL3_2" -> 1
      ),
    List.fill(7)(1),
    List(2, 0)
  )

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def  projectInfoTest15 = projectInfoTest("projectInfoTest15", 3, Map.empty, List.empty, List.empty)

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def  projectInfoTest16 = projectInfoTest("projectInfoTest16", 0, Map.empty, List.empty, List.empty)

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  @Test
  def  projectInfoTest17 = projectInfoTest("projectInfoTest17", 31,
    Map(
      "E1" -> 1,
      "E2" -> 1,
      "E3" -> 1,
      "E4" -> 1,
      "E5" -> 1,
      "E6" -> 1,
      "E7" -> 1,
      "E8" -> 1,
      "E9" -> 1,
      "E10" -> 1,
      "E11" -> 1,
      "E12" -> 1
    ),
    List.fill(12)(1),
    List(3, 2, 1))

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  def projectInfoTest(testName:String, expectedLoc:Int, expectedSd:Map[String, Int], td:List[Int], bmnd:List[Int]) = {
    val foundInfo = new CProjectInfo(getResource(testName))

    if (verbose) {
      println(foundInfo)
    }

    assertTrue(foundInfo.loc == expectedLoc)
    assertTrue(foundInfo.ftConstants == expectedSd.keys.toSet)
    assertTrue(foundInfo.sd == expectedSd)

    assertTrue(foundInfo.td.map(entry => entry._3).sortWith(_<=_) == td.sortWith(_<=_))
    assertTrue(foundInfo.td.find(entry => entry._2 < 0) == None)

    assertTrue(foundInfo.bmnd.map(entry => entry._3).sortWith(_<=_) == bmnd.sortWith(_<=_))
    assertTrue(foundInfo.bmnd.find(entry => entry._2 < 0) == None)
  }
}
