package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.data.Node
import kpn.api.custom.Fact
import kpn.api.custom.ScopedNetworkType
import kpn.core.util.Util
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeInfo

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

  def alternateNames(facts: ListBuffer[Fact], routeNodeInfos: Seq[RouteNodeInfo]): Map[Long /*nodeId*/ , String /*alternateName*/ ] = {
    if (routeNodeInfos.size < 2) {
      Map.empty
    }
    else {
      val suffixes = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
      if (routeNodeInfos.size > suffixes.length) {
        facts.addOne(Fact.RouteAnalysisFailed)
      }
      routeNodeInfos.zip(suffixes).map { case (routeNodeInfo, letter) =>
        routeNodeInfo.node.id -> (routeNodeInfo.name + "." + letter)
      }.toMap
    }
  }

}
