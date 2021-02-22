package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.data.Node
import kpn.api.custom.Fact
import kpn.api.custom.ScopedNetworkType
import kpn.core.util.Util

import scala.collection.mutable.ListBuffer

object NodeUtil {
  private val allDigits = """(\d*)""".r

  def toNumber(nodeName: String): Option[Int] = {
    nodeName match {
      case allDigits(number) => Some(number.toInt)
      case _ => None
    }
  }
}

class NodeUtil(scopedNetworkType: ScopedNetworkType) {

  def sortNames(nodeNames: Iterable[String]): Seq[String] = {
    if (nodeNames.exists(nodeName => !Util.isDigits(nodeName))) {
      // string sort
      nodeNames.toSeq.sorted
    }
    else {
      // numeric sort
      nodeNames.toSeq.sortBy(_.toInt)
    }
  }

  def sortByName(nodes: Iterable[Node], nameGetter: Node => String): Seq[Node] = {
    val nodeNames = nodes.map(node => nameGetter(node))
    if (nodeNames.exists(n => !Util.isDigits(n))) {
      nodes.toSeq.sortBy(node => nameGetter(node))
    }
    else {
      nodes.toSeq.sortBy(node => nameGetter(node).toInt)
    }
  }

  def alternateNames(facts: ListBuffer[Fact], nodes: Seq[Node], nameGetter: Node => String): Map[Long /*nodeId*/ , String /*alternateName*/ ] = {
    if (nodes.size < 2) {
      Map()
    }
    else {
      val suffixes = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
      if (nodes.size > suffixes.length) {
        facts.addOne(Fact.RouteAnalysisFailed)
      }
      nodes.zip(suffixes).map { case (node, letter) =>
        node.id -> (nameGetter(node) + "." + letter)
      }.toMap
    }
  }

}
