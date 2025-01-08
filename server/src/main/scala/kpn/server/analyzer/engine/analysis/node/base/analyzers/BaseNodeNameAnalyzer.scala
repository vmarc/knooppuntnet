package kpn.server.analyzer.engine.analysis.node.base.analyzers

import kpn.api.common.NodeName
import kpn.api.common.data.Tagable
import kpn.api.custom.ScopedNetworkType
import kpn.server.analyzer.engine.analysis.node.NodeUtil
import kpn.server.analyzer.engine.analysis.node.analyzers.Name

object BaseNodeNameAnalyzer extends BaseNodeAnalyzer {
  def analyze(context: BaseNodeAnalysisContext): BaseNodeAnalysisContext = {
    val names = findNodeNames(context.node)
    val name = if (names.nonEmpty) Some(names.map(_.name).distinct.mkString(" / ")) else None
    context.copy(
      _name = Some(name),
      _names = Some(names)
    )
  }

  private def findNodeNames(tagable: Tagable): Seq[NodeName] = {
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
}
