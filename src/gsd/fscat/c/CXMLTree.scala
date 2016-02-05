package gsd.fscat.c

import java.io.{InputStreamReader, File}
import scala.xml.{NodeSeq, XML, Elem}

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Passos
 * Date: 20/08/14
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
object CXMLTree {

  def apply(f:File) : Elem = {

    val src2srcml = Runtime.getRuntime().exec("src2srcml --position --language=C++ --cpp --cpp-markup-else --cpp-text-if0 " +
                                               f.getAbsolutePath)


    val xml =  XML.load(new InputStreamReader(src2srcml.getInputStream()))
    src2srcml.waitFor()

    //println(xml)

    return xml
  }

  def cppNamespace = "http://www.sdml.info/srcML/cpp"

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  def srcNamespace = "http://www.sdml.info/srcML/src"

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  def posNamespace = "http://www.sdml.info/srcML/position"

}
