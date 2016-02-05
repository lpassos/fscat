package gsd.fscat.c

import java.io.File
import scala.language.postfixOps

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Passos
 * Date: 20/08/14
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */


object  NormalizedCFile {

  def apply(in:File, out:File=null) : File = {

    import scala.sys.process._

    val tmp = File.createTempFile("norm", "idx")
    val dest = if (out == null) in else out

    val decomment = "stripcomments " + in.getAbsolutePath
    val sline = "sline "

    decomment #| sline #> tmp !  ;
    ("mv " + tmp.getAbsolutePath + " " + dest.getAbsolutePath) .! ;

    return dest
  }
}