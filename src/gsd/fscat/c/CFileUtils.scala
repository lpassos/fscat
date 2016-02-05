package gsd.fscat.c

import java.io.File
import gsd.utils.predicate.Predicate
import gsd.utils.io.FileUtils

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Passos
 * Date: 20/08/14
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
object CFileUtils {

  def isCFile(fileName:String) : Boolean = isCHeader(fileName) || isCImpl(fileName)
  def isCFile(file:File) : Boolean = isCFile(file.getName)

  def isCHeader(f:File) : Boolean =  isCHeader(f.getName)
  def isCHeader(fileName:String) : Boolean = headerExtensions.contains(FileUtils.getExtension(fileName))

  def isCImpl(f:File) : Boolean = isCImpl(f.getName)
  def isCImpl(fileName:String) : Boolean = implExtensions.contains(FileUtils.getExtension(fileName))

  val headerExtensions = Array("h", "hxx", "hpp", "inl")
  val implExtensions = Array("c", "cc", "cpp", "cxx")

  val extensionPredicate = new Predicate[File] {
    override def holds(f:File) = isCFile(f)
  }
}
