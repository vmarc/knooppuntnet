package kpn.core.tools.monitor.support

import kpn.core.data.Data
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelationTopLevel
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.wayToWorldCoordinates
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.monitor.repository.MonitorGroupRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl
import kpn.server.monitor.MonitorUtil
import kpn.server.monitor.domain.MonitorRelation
import kpn.server.monitor.repository.MonitorRelationRepositoryImpl
import kpn.server.monitor.route.update.RelationTopLevelDataBuilder
import org.locationtech.jts.geom.LineSegment

import scala.xml.XML

class MonitorCreateRelationsToolConfig(database: Database) {
  val groupRepository = new MonitorGroupRepositoryImpl(database)
  val routeRepository = new MonitorRouteRepositoryImpl(database)
  val relationRepository = new MonitorRelationRepositoryImpl(database)
  val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
  val lineSegmentTileCalculator = new LineSegmentTileCalculatorImpl(new TileCalculatorImpl())
}

object MonitorCreateRelationsTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-monitor") { database =>
      val config = new MonitorCreateRelationsToolConfig(database)
      val tool = new MonitorCreateRelationsTool(config)
      tool.createMonitorRelations()
    }
  }
}

class MonitorCreateRelationsTool(config: MonitorCreateRelationsToolConfig) {

  private val log = Log(classOf[MonitorCreateRelationsTool])

  def createMonitorRelations(): Unit = {
    val relationIds = collectRelationIds()
    log.info(s"collected ${relationIds.size} relation ids")
    relationIds.zipWithIndex.foreach { case (relationId, index) =>
      log.info(s"${index + 1}/${relationIds.size}")
      val monitorRelation = createMonitorRelation(relationId)
      config.relationRepository.save(monitorRelation)
    }
  }

  private def collectRelationIds(): Seq[Long] = {
    val ids = config.groupRepository.groups().sortBy(_.name).flatMap { group =>
      log.info(s"group ${group.name}")
      config.groupRepository.groupRoutes(group._id).sortBy(_.name).flatMap { route =>
        log.info(s"  route ${route.name}")
        route.relation.toSeq.map(_.relationId) ++
          MonitorUtil.subRelationsIn(route).map(_.relationId)
      }
    }
    ids.distinct.sorted
  }

  private def createMonitorRelation(relationId: Long): MonitorRelation = {
    val data = monitorRelationData(relationId)
    val elementIds = determineElementIds(data)
    val tiles = determineTiles(data)
    MonitorRelation(
      relationId,
      tiles.map(_.name),
      elementIds
    )
  }

  private def monitorRelationData(relationId: Long): Data = {
    val xmlString = config.overpassQueryExecutor.executeQuery(None, QueryRelationTopLevel(relationId))
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    new RelationTopLevelDataBuilder(rawData, relationId).data
  }

  private def determineElementIds(data: Data): ElementIds = {
    val nodes = data.nodes.values.toSeq
    val ways = data.ways.values.toSeq
    val nodeIds = nodes.map(_.id).toSet
    val wayIds = ways.map(_.id).toSet
    val subRelationIds = data.relations.values.flatMap(_.raw.relationMembers.map(_.ref)).toSet
    ElementIds(nodeIds, wayIds, subRelationIds)
  }

  private def determineTiles(data: Data): Seq[Tile] = {
    data.ways.values.toSeq.flatMap { way =>
      val worldCoordinates = wayToWorldCoordinates(way)
      val lineSegments = worldCoordinates.sliding(2).map { case Seq(c1, c2) =>
        new LineSegment(c1, c2)
      }.toSeq
      (2 to 14).flatMap { z =>
        config.lineSegmentTileCalculator.tiles(z, lineSegments)
      }
    }.distinct.sortBy(tile => (tile.z, tile.x, tile.y))
  }
}
