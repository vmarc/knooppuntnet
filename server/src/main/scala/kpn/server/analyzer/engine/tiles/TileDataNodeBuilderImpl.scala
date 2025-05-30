package kpn.server.analyzer.engine.tiles

import kpn.api.common.Fact
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.core.analysis.Facts
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import org.springframework.stereotype.Component

import scala.util.Failure
import scala.util.Success

@Component
class TileDataNodeBuilderImpl extends TileDataNodeBuilder {

  private case class NodeNameParts(
    ref: Option[String],
    name: Option[String]
  )

  private val prioritizedScopes = Seq(
    RouteScope.regional, // prefer regional over local
    RouteScope.local,
    RouteScope.national,
    RouteScope.international
  )

  def build(routeType: RouteType, nodeTileInfo: NodeTileInfo): Option[TileDataNode] = {

    calculatePrioritizedNodeName(routeType, nodeTileInfo).map { nodeName =>

      val parts = nodeName.longName match {
        case None => NodeNameParts(Some(nodeName.name), None)
        case Some(longName) =>
          if (longName == nodeName.name) {
            if (longName.length <= 3) {
              NodeNameParts(Some(longName), None)
            }
            else {
              NodeNameParts(None, Some(longName))
            }
          }
          else {
            NodeNameParts(Some(nodeName.name), Some(longName))
          }
      }

      val surveyDateTry = SurveyDateAnalyzer.analyze(nodeTileInfo)
      val surveyDate = surveyDateTry match {
        case Success(date) => date
        case Failure(_) => None
      }

      val proposed = nodeName.proposed ||
        nodeTileInfo.hasTag("state", "proposed")

      TileDataNode(
        nodeTileInfo.nodeId,
        parts.ref,
        parts.name,
        nodeTileInfo.latitude,
        nodeTileInfo.longitude,
        layer(nodeTileInfo.facts),
        surveyDate,
        proposed
      )
    }
  }

  private def calculatePrioritizedNodeName(routeType: RouteType, nodeTileInfo: NodeTileInfo) = {
    val unprioritizedNames = nodeTileInfo.names
      .filter(_.routeType == routeType)
      .filterNot(_.name == "o")
    prioritizedScopes.flatMap { scope =>
      unprioritizedNames.filter(_.routeScope == scope)
    }.headOption
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
    facts.exists(Facts.isError)
  }
}
