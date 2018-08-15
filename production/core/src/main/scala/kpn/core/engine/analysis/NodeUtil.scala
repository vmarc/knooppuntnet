package kpn.core.engine.analysis

import kpn.shared.NetworkType
import kpn.shared.data.Node

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

  def name(node: Node): String = {
    node.tags(networkType.nodeTagKey).getOrElse("")
  }

  def isNetworkNode(node: Node): Boolean = {
    node.tags.has(networkType.nodeTagKey)
  }

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

  def alternateNames(nodes: Seq[Node]): Map[Long /*nodeId*/, String /*alternateName*/] = {
    if (nodes.size < 2) {
      Map()
    }
    else {
      val suffixes = "abcdefghij"
      if (nodes.size > suffixes.length) {
        throw new IllegalArgumentException(s"Number of nodes (${nodes.size}) exceeds the expected maximum number of nodes (${suffixes.length})")
      }
      nodes.zip("abcdefghij").map { case(node, letter) =>
        node.id -> (name(node) + "." + letter)
      }.toMap
    }
  }

  private def isDigits(string: String): Boolean = string.nonEmpty && string.filterNot(_.isDigit).isEmpty
}
