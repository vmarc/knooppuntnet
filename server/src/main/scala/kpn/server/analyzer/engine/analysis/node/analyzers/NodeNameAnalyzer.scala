package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.NodeName
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.server.analyzer.engine.analysis.node.Name
import kpn.server.analyzer.engine.analysis.node.NodeUtil
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

object NodeNameAnalyzer extends NodeAspectAnalyzer {

  def nodeTagKeys(scopedNetworkType: ScopedNetworkType): Seq[String] = {
    val key = scopedNetworkType.key
    Seq(
      scopedNetworkType.nodeRefTagKey,
      scopedNetworkType.nodeNameTagKey,
      s"$key:name",
      s"name:${key}_ref",
      scopedNetworkType.proposedNodeRefTagKey,
      scopedNetworkType.proposedNodeNameTagKey,
      s"proposed:$key:name",
      s"proposed:name:${key}_ref"
    )
  }

  def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    new NodeNameAnalyzer(analysis).analyze
  }
}

class NodeNameAnalyzer(analysis: NodeAnalysis) {

  def analyze: NodeAnalysis = {
    val name = findName()
    val nodeNames = findNodeNames()
    analysis.copy(name = name, nodeNames = nodeNames)
  }

  private def findNodeNames(): Seq[NodeName] = {
    ScopedNetworkType.all.flatMap { scopedNetworkType =>
      determineScopedName(scopedNetworkType).map { name =>
        val longName = determineScopedLongName(scopedNetworkType)
        NodeName(
          scopedNetworkType.networkType,
          scopedNetworkType.networkScope,
          name.name,
          longName.map(_.name),
          proposed = name.proposed
        )
      }
    }
  }

  private def findName(): String = {
    NetworkType.all.flatMap { networkType =>
      NetworkScope.all.flatMap { networkScope =>
        scopedName(ScopedNetworkType.from(networkScope, networkType))
      }.distinct
    }.mkString(" / ")
  }

  private def scopedName(scopedNetworkType: ScopedNetworkType): Option[String] = {
    determineScopedName(scopedNetworkType).map(_.name)
  }

  private def determineScopedName(scopedNetworkType: ScopedNetworkType): Option[Name] = {
    val nameOption = analysis.node.tags(scopedNetworkType.nodeRefTagKey) match {
      case Some(name) => Some(Name(name, proposed = stateProposed()))
      case None =>
        analysis.node.tags(scopedNetworkType.proposedNodeRefTagKey) match {
          case Some(name) => Some(Name(name, proposed = true))
          case None => determineScopedLongName(scopedNetworkType)
        }
    }
    nameOption.map(n => n.copy(name = NodeUtil.normalize(n.name)))
  }

  private def determineScopedLongName(scopedNetworkType: ScopedNetworkType): Option[Name] = {
    val prefix = scopedNetworkType.key
    val nameTagKeys = Seq(
      s"${prefix}_name",
      s"$prefix:name",
      s"name:${prefix}_ref"
    )
    val proposedNameTagKeys = Seq(
      s"proposed:${prefix}_name",
      s"proposed:$prefix:name",
      s"proposed:name:${prefix}_ref"
    )
    analysis.node.tags.tags.find(tag => nameTagKeys.contains(tag.key)).map(_.value) match {
      case Some(name) => Some(Name(name, stateProposed()))
      case None =>
        analysis.node.tags.tags.find(tag => proposedNameTagKeys.contains(tag.key)).map(_.value).map { name =>
          Name(name, proposed = true)
        }
    }
  }

  private def stateProposed(): Boolean = {
    analysis.node.tags.has("state", "proposed")
  }
}
