package kpn.core.tools.monitor.support

import kpn.api.custom.Relation
import kpn.core.data.Data
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
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
  private val batchSize = 100

  def createMonitorRelations(): Unit = {
    val routeIds = collectRouteIds()
    log.info(s"collected ${routeIds.size} route ids")
    routeIds.sliding(batchSize, batchSize).zipWithIndex.foreach { case (batchRouteIds, index) =>
      log.info(s"${index * batchSize}/${routeIds.size}")
      val monitorRelations = createMonitorRelations(batchRouteIds)
      monitorRelations.foreach(config.relationRepository.save)
    }
  }

  private def collectRouteIds(): Seq[Long] = {
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

  private def createMonitorRelations(routeRelationIds: Seq[Long]): Seq[MonitorRelation] = {
    val data = monitorRelationData(routeRelationIds)
    routeRelationIds.flatMap { routeRelationId =>
      data.relations.get(routeRelationId) match {
        case Some(relation) =>
          val elementIds = determineElementIds(relation)
          val tiles = determineTiles(relation)
          Some(
            MonitorRelation(
              routeRelationId,
              tiles.map(_.name),
              elementIds
            )
          )
        case None =>
          log.error(s"could route routeId $routeRelationId")
          None
      }
    }
  }

  private def monitorRelationData(relationIds: Seq[Long]): Data = {
    val relations = relationIds.map(id => s"relation($id);").mkString
    val query = s"($relations);(._;>;);out meta;"
    val xmlString = config.overpassQueryExecutor.execute(query)
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    new RelationTopLevelDataBuilder(rawData, relationIds).data
  }

  private def determineElementIds(relation: Relation): ElementIds = {
    val memberNodeIds = relation.nodeMembers.map(_.node.id)
    val wayNodeIds = relation.wayMembers.flatMap(member => member.way.nodes.map(_.id))
    val nodeIds = memberNodeIds.toSet ++ wayNodeIds.toSet
    val wayIds = relation.wayMembers.map(_.way.id).toSet
    val subRelationIds = relation.relationMembers.map(_.relation.id).toSet
    ElementIds(nodeIds, wayIds, subRelationIds)
  }

  private def determineTiles(relation: Relation): Seq[Tile] = {
    relation.wayMembers.map(_.way).flatMap { way =>
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
