package kpn.server.analyzer.engine.tiles

import kpn.api.common.NodeName
import kpn.api.custom.Fact
import kpn.api.custom.FactLevel
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import org.springframework.stereotype.Component

import scala.util.Failure
import scala.util.Success

@Component
class TileDataNodeBuilderImpl() extends TileDataNodeBuilder {

  private val prioritizedScopes = Seq(
    NetworkScope.regional, // prefer regional over local
    NetworkScope.local,
    NetworkScope.national,
    NetworkScope.international
  )

  def build(networkType: NetworkType, node: NodeTileInfo): Option[TileDataNode] = {

    val nodeNameOption: Option[NodeName] = {
      val unprioritizedNames = node.names
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

      val surveyDateTry = SurveyDateAnalyzer.analyze(node.tags)
      val surveyDate = surveyDateTry match {
        case Success(surveyDate) => surveyDate
        case Failure(_) => None
      }

      val proposed = nodeNameOption.exists(_.proposed) ||
        node.tags.has("state", "proposed")

      TileDataNode(
        node._id,
        ref,
        name,
        node.latitude,
        node.longitude,
        layer(node.facts),
        surveyDate,
        proposed
      )
    }
  }

  private def layer(facts: Seq[Fact]): String = {
    if (hasError(facts)) {
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
