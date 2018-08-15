package kpn.core.tiles

import com.vividsolutions.jts.geom.GeometryFactory
import kpn.core.tiles.domain.TileCache
import kpn.core.tiles.domain.TileNodes
import kpn.core.tiles.domain.TileRoute
import kpn.core.tiles.domain.TileRoutes
import kpn.core.util.Log
import kpn.shared.NetworkType
import kpn.shared.NodeInfo
import kpn.shared.network.NetworkNodeInfo2
import kpn.shared.node.NodeNameAnalyzer.name
import kpn.shared.route.RouteInfo

class TilesBuilder(
  tileBuilder: TileBuilder,
  tileRepository: TileRepository
) {

  private val log = Log(classOf[TilesBuilder])

  private val NODE_MIN_ZOOM_LEVEL = 9

  private val geomFactory = new GeometryFactory

  private val tileCache = new TileCache()

  def build(
    z: Int,
    analysis: TileAnalysis
  ): Unit = {

    val existingTileNames = tileRepository.existingTileNames(analysis.networkType, z)

    log.info(s"Processing zoomlevel $z")
    log.info(s"Number of tiles before: " + existingTileNames.size)

    log.info(s"buildTileNodeMap()")
    val tileNodes = buildTileNodeMap(analysis.networkType, z, analysis.nodes, analysis.orphanNodes)
    log.info(s"buildTileRoutes()")
    val tileRoutes = buildTileRoutes(z, analysis.routeInfos)
    log.info(s"buildTileRouteMap()")
    val tileRoutesMap = buildTileRouteMap(z, tileRoutes)
    val tileNames = (tileNodes.keys ++ tileRoutesMap.keys).toSet.toSeq
    log.info(s"build ${tileNames.size} tiles")

    var progress: Int = 0

    tileNames.zipWithIndex.foreach { case (tileName: String, index) =>
      val currentProgress = (100d * (index + 1) / tileNames.size).round.toInt
      if (currentProgress != progress) {
        progress = currentProgress
        log.info(s"Build tile ${index + 1}/${tileNames.size} $progress $tileName")
      }

      val tileNodesOption = tileNodes.get(tileName)
      val tileRoutesOption = tileRoutesMap.get(tileName)

      val tile = tileNodesOption match { // TODO MAP can do this cleaner?
        case Some(tileNodes1) => tileNodes1.tile
        case None => tileRoutesOption match {
          case Some(tileRoutes1) => tileRoutes1.tile
          case None => throw new IllegalStateException()
        }
      }


      val nodes = tileNodesOption match {
        case None => Seq()
        case Some(tn) => tn.nodes
      }
      val routes = tileRoutesOption match {
        case None => Seq()
        case Some(tr) => tr.routes
      }

      val tileData = TileData(
        analysis.networkType,
        tile,
        nodes,
        routes
      )

      val tileBytes = tileBuilder.build(tileData)
      if (tileBytes.length > 0) {
        tileRepository.saveOrUpdate(analysis.networkType, tile, tileBytes)
      }
    }

    val afterTileNames = tileNames.map(tileName => analysis.networkType.name + "-" + tileName)

    val obsoleteTileNames = (existingTileNames.toSet -- afterTileNames.toSet).toSeq.sorted
    log.info(s"Obsolete: " + obsoleteTileNames)

    log.info(s"Obsolete tile count: " + obsoleteTileNames.size)
    tileRepository.delete(obsoleteTileNames)
    log.info(s"Obsolete tiles removed")
  }

  private def buildTileNodeMap(networkType: NetworkType, z: Int, nodes: Seq[NetworkNodeInfo2], orphanNodes: Seq[NodeInfo]): Map[String, TileNodes] = {
    if (z < NODE_MIN_ZOOM_LEVEL) {
      Map()
    }
    else {
      val allNodes = nodes ++ orphanNodes.map { node =>
        NetworkNodeInfo2(
          node.id,
          title = name(networkType, node.tags),
          number = "",
          latitude = node.latitude,
          longitude = node.longitude,
          connection = false,
          definedInRelation = false,
          definedInRoute = false,
          timestamp = node.lastUpdated,
          routeReferences = Seq(),
          integrityCheck = None,
          tags = node.tags
        )
      }

      val analyzer = new NodeTileAnalyzer(tileCache)
      val map = scala.collection.mutable.Map[String, TileNodes]()

      allNodes.foreach { node =>
        val tiles = analyzer.tiles(z, node)
        tiles.foreach { tile =>
          map(tile.name) = map.get(tile.name) match {
            case Some(tileNodes) => TileNodes(tile, tileNodes.nodes :+ node)
            case None => TileNodes(tile, Seq(node))
          }
        }
      }
      map.toMap
    }
  }

  private def buildTileRouteMap(z: Int, tileRoutes: Seq[TileRoute]): Map[String, TileRoutes] = {

    val map = scala.collection.mutable.Map[String, TileRoutes]()

    val routeTileAnalyzer = new RouteTileAnalyzer(tileCache)

    var progress: Int = 0
    tileRoutes.zipWithIndex.foreach { case (tileRoute, index) =>
      val tiles = routeTileAnalyzer.tiles(z, tileRoute)
      val currentProgress = (100d * (index + 1) / tileRoutes.size).round.toInt
      if (currentProgress != progress) {
        progress = currentProgress
        log.info(s"Build route map ${index + 1}/${tileRoutes.size} $progress% tilecount=${tiles.size}")
      }
      tiles.foreach { tile =>
        map(tile.name) = map.get(tile.name) match {
          case Some(tileRoutes1) => TileRoutes(tile, tileRoutes1.routes :+ tileRoute)
          case None => TileRoutes(tile, Seq(tileRoute))
        }
      }
    }
    map.toMap
  }

  private def buildTileRoutes(z: Int, routeInfos: Seq[RouteInfo]): Seq[TileRoute] = {
    val b = new TileRouteBuilder(z)
    routeInfos.flatMap(b.build)
  }
}
