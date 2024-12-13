package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.NetworkType
import kpn.api.common.NodeName
import kpn.api.common.data.Tagable
import kpn.api.custom.NetworkScope
import kpn.api.custom.ScopedNetworkType
import kpn.server.analyzer.engine.analysis.node.NodeUtil
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

case class Name(name: String, proposed: Boolean)

object NodeNameAnalyzer extends NodeAspectAnalyzer {

  def findNodeNames(tagable: Tagable): Seq[NodeName] = {
    ScopedNetworkType.all.flatMap { scopedNetworkType =>
      determineScopedName(tagable, scopedNetworkType).map { name =>
        val longNameOption = determineScopedLongName(tagable, scopedNetworkType) match {
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

  def findName(tagable: Tagable): String = {
    NetworkType.values.flatMap { networkType =>
      NetworkScope.all.flatMap { networkScope =>
        scopedName(tagable, ScopedNetworkType.from(networkScope, networkType))
      }.distinct
    }.mkString(" / ")
  }

  private def scopedName(tagable: Tagable, scopedNetworkType: ScopedNetworkType): Option[String] = {
    determineScopedName(tagable, scopedNetworkType).map(_.name)
  }

  private def determineScopedName(tagable: Tagable, scopedNetworkType: ScopedNetworkType): Option[Name] = {
    val nameOption = tagable.tagValue(scopedNetworkType.nodeRefTagKey) match {
      case Some(name) => Some(Name(name, proposed = stateProposed(tagable)))
      case None =>
        tagable.tagValue(scopedNetworkType.proposedNodeRefTagKey) match {
          case Some(name) => Some(Name(name, proposed = true))
          case None => determineScopedLongName(tagable, scopedNetworkType)
        }
    }
    nameOption.map(n => n.copy(name = NodeUtil.normalize(n.name)))
  }

  private def determineScopedLongName(tagable: Tagable, scopedNetworkType: ScopedNetworkType): Option[Name] = {
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
    tagable.tags.find(tag => nameTagKeys.contains(tag.key)).map(_.value) match {
      case Some(name) => Some(Name(name, stateProposed(tagable)))
      case None =>
        tagable.tags.find(tag => proposedNameTagKeys.contains(tag.key)).map(_.value).map { name =>
          Name(name, proposed = true)
        }
    }
  }

  private def stateProposed(tagable: Tagable): Boolean = {
    tagable.hasTag("state", "proposed")
  }

  def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    new NodeNameAnalyzer(analysis).analyze
  }
}

class NodeNameAnalyzer(analysis: NodeAnalysis) {

  def analyze: NodeAnalysis = {
    val name = NodeNameAnalyzer.findName(analysis.node)
    val nodeNames = NodeNameAnalyzer.findNodeNames(analysis.node)
    if (name.isEmpty) {
      analysis.copy(abort = true)
    }
    else {
      analysis.copy(
        name = name,
        nodeNames = nodeNames
      )
    }
  }
}
