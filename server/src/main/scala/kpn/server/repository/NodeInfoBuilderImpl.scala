package kpn.server.repository

import kpn.api.common.LatLonImpl
import kpn.api.common.NodeInfo
import kpn.api.common.data.raw.RawNode
import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.analysis.NetworkNodeInfo
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer
import kpn.server.analyzer.engine.analysis.location.NodeLocationAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.tile.NodeTileAnalyzer
import kpn.server.analyzer.load.data.LoadedNode
import org.springframework.stereotype.Component

import scala.util.Failure
import scala.util.Success

@Component
class NodeInfoBuilderImpl(
  nodeTileAnalyzer: NodeTileAnalyzer,
  //  countryAnalyzer: CountryAnalyzer,
  nodeLocationAnalyzer: NodeLocationAnalyzer
) extends NodeInfoBuilder {

  def build(
    id: Long,
    active: Boolean,
    orphan: Boolean,
    country: Option[Country],
    latitude: String,
    longitude: String,
    lastUpdated: Timestamp,
    tags: Tags,
    facts: Seq[Fact]
  ): NodeInfo = {

    val nodeNames = NodeAnalyzer.names(tags)

    //val country = countryAnalyzer.country(Seq(LatLonImpl(latitude, longitude)))
    val location = nodeLocationAnalyzer.locate(latitude, longitude)

    val tiles = {
      val tiles = (ZoomLevel.nodeMinZoom to ZoomLevel.vectorTileMaxZoom).flatMap { z =>
        nodeTileAnalyzer.tiles(z, LatLonImpl(latitude, longitude))
      }
      tiles.flatMap { tile =>
        nodeNames.map(_.scopedNetworkType.networkType).map { networkType =>
          s"${networkType.name}-${tile.name}"
        }
      }
    }

    val surveyDateTry = SurveyDateAnalyzer.analyze(tags)
    val surveyDate = surveyDateTry match {
      case Success(v) => v
      case Failure(_) => None
    }

    val updatedFacts = surveyDateTry match {
      case Success(v) => facts
      case Failure(_) => facts :+ Fact.NodeInvalidSurveyDate
    }

    NodeInfo(
      id,
      active,
      orphan,
      country,
      NodeAnalyzer.name(tags),
      nodeNames,
      latitude,
      longitude,
      lastUpdated,
      surveyDate,
      tags,
      updatedFacts,
      location,
      tiles
    )
  }

  def fromLoadedNode(
    loadedNode: LoadedNode,
    active: Boolean = true,
    orphan: Boolean = false,
    facts: Seq[Fact] = Seq.empty
  ): NodeInfo = {
    build(
      loadedNode.node.id,
      active,
      orphan,
      loadedNode.country,
      loadedNode.node.latitude,
      loadedNode.node.longitude,
      loadedNode.node.timestamp,
      loadedNode.node.tags,
      facts
    )
  }

  def fromNetworkNodeInfo(
    networkNodeInfo: NetworkNodeInfo,
    active: Boolean = true,
    facts: Seq[Fact] = Seq.empty
  ): NodeInfo = {
    val node = networkNodeInfo.networkNode.node
    build(
      node.id,
      active,
      orphan = false,
      networkNodeInfo.networkNode.country,
      node.latitude,
      node.longitude,
      node.timestamp,
      node.tags,
      facts
    )
  }

  def fromRawNode(
    rawNode: RawNode,
    country: Option[Country],
    active: Boolean = true,
    orphan: Boolean = false,
    facts: Seq[Fact] = Seq.empty
  ): NodeInfo = {
    build(
      rawNode.id,
      active,
      orphan,
      country,
      rawNode.latitude,
      rawNode.longitude,
      rawNode.timestamp,
      rawNode.tags,
      facts
    )
  }
}
