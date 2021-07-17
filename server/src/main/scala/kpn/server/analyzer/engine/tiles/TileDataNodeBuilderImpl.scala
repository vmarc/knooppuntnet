package kpn.server.analyzer.engine.tiles

import kpn.api.common.NodeInfo
import kpn.api.common.NodeName
import kpn.api.common.network.NetworkInfoNode
import kpn.api.custom.Fact
import kpn.api.custom.FactLevel
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalyzer
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import org.springframework.stereotype.Component

import scala.util.Failure
import scala.util.Success

@Component
class TileDataNodeBuilderImpl(nodeAnalyzer: OldNodeAnalyzer) extends TileDataNodeBuilder {

  private val prioritizedScopes = Seq(
    NetworkScope.regional, // prefer regional over local
    NetworkScope.local,
    NetworkScope.national,
    NetworkScope.international
  )

  def build(networkType: NetworkType, node: NodeInfo): Option[TileDataNode] = {
    doBuild(
      networkType,
      node.id,
      node.latitude,
      node.longitude,
      node.orphan,
      node.facts,
      node.tags
    )
  }

  def build(networkType: NetworkType, node: NetworkInfoNode): Option[TileDataNode] = {
    doBuild(
      networkType,
      node.id,
      node.latitude,
      node.longitude,
      node.facts.contains(Fact.OrphanNode),
      node.facts,
      node.tags
    )
  }

  private def doBuild(
    networkType: NetworkType,
    id: Long,
    latitude: String,
    longitude: String,
    orphan: Boolean,
    facts: Seq[Fact],
    tags: Tags
  ): Option[TileDataNode] = {

    val nodeNameOption: Option[NodeName] = {
      val unprioritizedNames = nodeAnalyzer
        .names(tags)
        .filter(_.networkType == networkType)
        .filterNot(_.name == "o")
      prioritizedScopes.flatMap { scope =>
        unprioritizedNames.filter(_.networkScope == scope)
      }.headOption
    }

    nodeNameOption.map { nodeName =>
      val (ref, name) = nodeName.longName match {
        case None => (Some(nodeName.name), None)
        case Some(longName) =>
          if (longName == nodeName.name) {
            if (longName.length <= 3) {
              (Some(longName), None)
            }
            else {
              (None, Some(longName))
            }
          }
          else {
            (Some(nodeName.name), Some(longName))
          }
      }

      val surveyDateTry = SurveyDateAnalyzer.analyze(tags)
      val surveyDate = surveyDateTry match {
        case Success(surveyDate) => surveyDate
        case Failure(_) => None
      }

      val proposed = nodeNameOption.exists(_.proposed) ||
        tags.has("state", "proposed")

      TileDataNode(
        id,
        ref,
        name,
        latitude,
        longitude,
        layer(orphan, facts),
        surveyDate,
        proposed
      )
    }
  }

  private def layer(orphan: Boolean, facts: Seq[Fact]): String = {
    if (orphan) {
      if (hasError(facts)) {
        "error-orphan-node"
      }
      else {
        "orphan-node"
      }
    }
    else if (hasError(facts)) {
      "error-node"
    }
    else {
      "node"
    }
  }

  private def hasError(facts: Seq[Fact]): Boolean = {
    facts.exists(_.level == FactLevel.ERROR)
  }
}
