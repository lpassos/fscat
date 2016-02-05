package gsd.fscat.xml

import scala.xml.{Elem, Node}
import scala.collection.mutable

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Passos
 * Date: 21/08/14
 * Time: 6:48 PM
 * To change this template use File | Settings | File Templates.
 */
object XMLUtils {

  def flat(n:Node) : Seq[Node] =  n ++ (n.child.length match {
      case 0 => Seq[Node]()
      case _ => n.child.map(flat(_)).foldLeft(Seq[Node]())(_++_)
  })

  def getIntAttr(n:Node, ns:String, attr:String) = n.attribute(ns, attr) match {
    case Some(line) => Some(Integer.parseInt(line.toString()))
    case None       => None
  }
}
