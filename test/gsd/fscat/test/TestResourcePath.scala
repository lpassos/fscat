package gsd.fscat.test

import java.io.File

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Passos
 * Date: 21/08/14
 * Time: 9:17 PM
 * To change this template use File | Settings | File Templates.
 */
class TestResourcePath(pkg:String) {

  private val baseLocation = "test" + File.separator + pkg.replace(".", File.separator)

  def getResource(name:String) : File = new File(baseLocation + File.separator + name)
}
