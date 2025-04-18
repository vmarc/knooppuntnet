package kpn.server.analyzer.engine.analysis.node.base.analyzers

import kpn.api.common.NodeName
import kpn.api.common.data.Tagable
import kpn.api.custom.ScopedRouteType
import kpn.server.analyzer.engine.analysis.node.NodeUtil

case class Name(name: String, proposed: Boolean)

object BaseNodeNameAnalyzer extends BaseNodeAnalyzer {
  def analyze(context: BaseNodeAnalysisContext): BaseNodeAnalysisContext = {
    val names = findNodeNames(context.node)
    val name = if (names.nonEmpty) Some(names.map(_.name).distinct.mkString(" / ")) else None
    val active = names.nonEmpty
    context.copy(
      _name = Some(name),
      _names = Some(names),
      active = active
    )
  }

  private def findNodeNames(tagable: Tagable): Seq[NodeName] = {
    ScopedRouteType.all.flatMap { scopedRouteType =>
      determineScopedName(tagable, scopedRouteType).map { name =>
        val longNameOption = determineScopedLongName(tagable, scopedRouteType) match {
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
          scopedRouteType.routeType,
          scopedRouteType.routeScope,
          name.name,
          longNameOption,
          proposed = name.proposed
        )
      }
    }
  }

  private def determineScopedName(tagable: Tagable, scopedRouteType: ScopedRouteType): Option[Name] = {
    val nameOption = tagable.tagValue(scopedRouteType.nodeRefTagKey) match {
      case Some(name) => Some(Name(name, proposed = stateProposed(tagable)))
      case None =>
        tagable.tagValue(scopedRouteType.proposedNodeRefTagKey) match {
          case Some(name) => Some(Name(name, proposed = true))
          case None => determineScopedLongName(tagable, scopedRouteType)
        }
    }
    nameOption.map(n => n.copy(name = NodeUtil.normalize(n.name)))
  }

  private def determineScopedLongName(tagable: Tagable, scopedRouteType: ScopedRouteType): Option[Name] = {
    val prefix = scopedRouteType.key
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
