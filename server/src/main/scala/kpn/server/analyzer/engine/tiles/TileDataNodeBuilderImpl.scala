package kpn.server.analyzer.engine.tiles

import kpn.api.common.Fact
import kpn.api.common.NodeName
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

  private val prioritizedScopes = Seq(
    RouteScope.regional, // prefer regional over local
    RouteScope.local,
    RouteScope.national,
    RouteScope.international
  )

  def build(routeType: RouteType, node: NodeTileInfo): Option[TileDataNode] = {

    val nodeNameOption: Option[NodeName] = {
      val unprioritizedNames = node.names
        .filter(_.routeType == routeType)
        .filterNot(_.name == "o")
      prioritizedScopes.flatMap { scope =>
        unprioritizedNames.filter(_.routeScope == scope)
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

      val surveyDateTry = SurveyDateAnalyzer.analyze(node)
      val surveyDate = surveyDateTry match {
        case Success(date) => date
        case Failure(_) => None
      }

      val proposed = nodeNameOption.exists(_.proposed) ||
        node.hasTag("state", "proposed")

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
    facts.exists(Facts.isError)
  }
}
