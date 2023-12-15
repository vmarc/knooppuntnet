package kpn.core.tools.monitor.support

import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelationTopLevel
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.analyzer.engine.tile.OldLinesTileCalculator
import kpn.server.analyzer.engine.tile.OldLinesTileCalculatorImpl
import kpn.server.analyzer.engine.tile.OldTileCalculatorImpl
import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.Point
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorGroupRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl
import kpn.server.monitor.MonitorUtil
import kpn.server.monitor.domain.MonitorRelation
import kpn.server.monitor.repository.MonitorRelationRepository
import kpn.server.monitor.repository.MonitorRelationRepositoryImpl
import kpn.server.monitor.route.update.RelationTopLevelDataBuilder

import scala.xml.XML

object MonitorCreateRelationsTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-monitor") { database =>
      val groupRepository = new MonitorGroupRepositoryImpl(database)
      val routeRepository = new MonitorRouteRepositoryImpl(database)
      val relationRepository = new MonitorRelationRepositoryImpl(database)
      val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
      val linesTileCalculator = new OldLinesTileCalculatorImpl(new OldTileCalculatorImpl())
      val tool = new MonitorCreateRelationsTool(
        groupRepository,
        routeRepository,
        relationRepository,
        overpassQueryExecutor,
        linesTileCalculator
      )
      tool.createMonitorRelations()
    }
  }
}

class MonitorCreateRelationsTool(
  groupRepository: MonitorGroupRepository,
  routeRepository: MonitorRouteRepository,
  relationRepository: MonitorRelationRepository,
  overpassQueryExecutor: OverpassQueryExecutor,
  linesTileCalculator: OldLinesTileCalculator
) {

  def createMonitorRelations(): Unit = {
    val relationIds = collectionRelationIds()
    println(s"collected ${relationIds.size} relation ids")
    relationIds.zipWithIndex.foreach { case (relationId, index) =>
      println(s"${index + 1}/${relationIds.size}")
      val monitorRelation = createMonitorRelation(relationId)
      relationRepository.save(monitorRelation)
    }
  }

  private def collectionRelationIds(): Seq[Long] = {
    val ids = groupRepository.groups().sortBy(_.name) flatMap { group =>
      println(s"group ${group.name}")
      groupRepository.groupRoutes(group._id).sortBy(_.name).flatMap { route =>
        println(s"  route ${route.name}")
        route.relation.toSeq.map(_.relationId) ++
          MonitorUtil.subRelationsIn(route).map(_.relationId)
      }
    }
    ids.distinct.sorted
  }

  private def createMonitorRelation(relationId: Long): MonitorRelation = {
    val xmlString = overpassQueryExecutor.executeQuery(None, QueryRelationTopLevel(relationId))
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    val data = new RelationTopLevelDataBuilder(rawData, relationId).data
    val nodes = data.nodes.values.toSeq
    val ways = data.ways.values.toSeq
    val nodeIds = nodes.map(_.id).toSet
    val wayIds = ways.map(_.id).toSet
    val subRelationIds = data.relations.values.flatMap(_.raw.relationMembers.map(_.ref)).toSet
    val elementIds = ElementIds(nodeIds, wayIds, subRelationIds)
    val tiles = ways.flatMap { way =>
      val lines = way.nodes.sliding(2).map { case Seq(node1, node2) =>
        Line(Point(node1.lon, node1.lat), Point(node2.lon, node2.lat))
      }.toSeq
      (2 to 14).flatMap { z =>
        linesTileCalculator.tiles(z, lines)
      }
    }.distinct.sortBy(tile => (tile.z, tile.x, tile.y))

    MonitorRelation(
      relationId,
      tiles.map(_.name),
      elementIds
    )
  }
}
