package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.NetworkScope
import kpn.api.common.NodeName
import kpn.api.common.RouteType
import kpn.api.common.data.Tagable
import kpn.api.custom.ScopedRouteType
import kpn.server.analyzer.engine.analysis.node.NodeUtil
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

case class Name(name: String, proposed: Boolean)

object NodeNameAnalyzer extends NodeAspectAnalyzer {

  def findNodeNames(tagable: Tagable): Seq[NodeName] = {
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
          scopedRouteType.networkScope,
          name.name,
          longNameOption,
          proposed = name.proposed
        )
      }
    }
  }

  def findName(tagable: Tagable): String = {
    RouteType.values.flatMap { routeType =>
      NetworkScope.values.flatMap { networkScope =>
        scopedName(tagable, ScopedRouteType.from(networkScope, routeType))
      }.distinct
    }.mkString(" / ")
  }

  private def scopedName(tagable: Tagable, scopedRouteType: ScopedRouteType): Option[String] = {
    determineScopedName(tagable, scopedRouteType).map(_.name)
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
