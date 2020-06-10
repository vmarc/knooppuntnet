package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.data.Node
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType

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

class NodeUtil(networkType: NetworkType) {

  def sortNames(nodeNames: Iterable[String]): Seq[String] = {
    if (nodeNames.exists(nodeName => !isDigits(nodeName))) {
      // string sort
      nodeNames.toSeq.sorted
    }
    else {
      // numeric sort
      nodeNames.toSeq.sortBy(_.toInt)
    }
  }

  def sortByName(nodes: Iterable[Node]): Seq[Node] = {
    val nodeNames = nodes.map(name)
    if (nodeNames.exists(n => !isDigits(n))) {
      nodes.toSeq.sortBy(name)
    }
    else {
      nodes.toSeq.sortBy(n => name(n).toInt)
    }
  }

  def alternateNames(facts: ListBuffer[Fact], nodes: Seq[Node]): Map[Long /*nodeId*/ , String /*alternateName*/ ] = {
    if (nodes.size < 2) {
      Map()
    }
    else {
      val suffixes = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
      if (nodes.size > suffixes.length) {
        facts.addOne(Fact.RouteAnalysisFailed)
      }
      nodes.zip(suffixes).map { case (node, letter) =>
        node.id -> (name(node) + "." + letter)
      }.toMap
    }
  }

  private def isDigits(string: String): Boolean = string.nonEmpty && string.filterNot(_.isDigit).isEmpty

  private def name(node: Node): String = {
    NodeAnalyzer.name(networkType, node.tags)
  }

}
