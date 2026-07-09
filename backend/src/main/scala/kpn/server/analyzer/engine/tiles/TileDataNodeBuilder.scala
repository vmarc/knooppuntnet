package kpn.server.analyzer.engine.tiles

import kpn.api.common.Fact
import kpn.api.common.FeatureLayer
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.core.analysis.Facts
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class TileDataNodeBuilder {

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

      val proposed = nodeName.proposed ||
        nodeTileInfo.hasTag("state", "proposed")

      TileDataNode(
        nodeTileInfo.nodeId,
        parts.ref,
        parts.name,
        nodeTileInfo.latitude,
        nodeTileInfo.longitude,
        layer(nodeTileInfo.facts),
        nodeTileInfo.lastSurvey,
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

  private def layer(facts: Seq[Fact]): FeatureLayer = {
    if (hasError(facts)) {
      FeatureLayer.errorNode
    }
    else {
      FeatureLayer.node
    }
  }

  private def hasError(facts: Seq[Fact]): Boolean = {
    facts.exists(Facts.isError)
  }
}
