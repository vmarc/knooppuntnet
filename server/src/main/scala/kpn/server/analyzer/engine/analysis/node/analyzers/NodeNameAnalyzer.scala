package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.NodeName
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.node.NodeUtil
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

case class Name(name: String, proposed: Boolean)

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

  def findNodeNames(tags: Tags): Seq[NodeName] = {
    ScopedNetworkType.all.flatMap { scopedNetworkType =>
      determineScopedName(tags, scopedNetworkType).map { name =>
        val longNameOption = determineScopedLongName(tags, scopedNetworkType) match {
          case None => None
          case Some(longName) =>
            if (longName.name != name.name) {
              Some(longName.name)
            }
            else {
              None
            }
        }
        NodeName(
          scopedNetworkType.networkType,
          scopedNetworkType.networkScope,
          name.name,
          longNameOption,
          proposed = name.proposed
        )
      }
    }
  }

  def findName(tags: Tags): String = {
    NetworkType.all.flatMap { networkType =>
      NetworkScope.all.flatMap { networkScope =>
        scopedName(tags, ScopedNetworkType.from(networkScope, networkType))
      }.distinct
    }.mkString(" / ")
  }

  private def scopedName(tags: Tags, scopedNetworkType: ScopedNetworkType): Option[String] = {
    determineScopedName(tags, scopedNetworkType).map(_.name)
  }

  private def determineScopedName(tags: Tags, scopedNetworkType: ScopedNetworkType): Option[Name] = {
    val nameOption = tags(scopedNetworkType.nodeRefTagKey) match {
      case Some(name) => Some(Name(name, proposed = stateProposed(tags)))
      case None =>
        tags(scopedNetworkType.proposedNodeRefTagKey) match {
          case Some(name) => Some(Name(name, proposed = true))
          case None => determineScopedLongName(tags, scopedNetworkType)
        }
    }
    nameOption.map(n => n.copy(name = NodeUtil.normalize(n.name)))
  }

  private def determineScopedLongName(tags: Tags, scopedNetworkType: ScopedNetworkType): Option[Name] = {
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
    tags.tags.find(tag => nameTagKeys.contains(tag.key)).map(_.value) match {
      case Some(name) => Some(Name(name, stateProposed(tags)))
      case None =>
        tags.tags.find(tag => proposedNameTagKeys.contains(tag.key)).map(_.value).map { name =>
          Name(name, proposed = true)
        }
    }
  }

  private def stateProposed(tags: Tags): Boolean = {
    tags.has("state", "proposed")
  }

  def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    new NodeNameAnalyzer(analysis).analyze
  }
}

class NodeNameAnalyzer(analysis: NodeAnalysis) {

  def analyze: NodeAnalysis = {
    val name = NodeNameAnalyzer.findName(analysis.node.tags)
    val nodeNames = NodeNameAnalyzer.findNodeNames(analysis.node.tags)
    analysis.copy(
      name = name,
      nodeNames = nodeNames
    )
  }
}
