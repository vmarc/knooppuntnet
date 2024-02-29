package kpn.core.tools.next.support

import kpn.api.custom.Relation
import kpn.core.tools.next.database.NextDatabase
import kpn.core.tools.next.database.NextDatabaseImpl
import kpn.core.tools.next.domain.NextRouteState
import kpn.core.util.Log
import kpn.database.util.Mongo.codecRegistry
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.wayToWorldCoordinates
import kpn.server.analyzer.engine.tiles.domain.Tile
import org.locationtech.jts.geom.LineSegment
import org.mongodb.scala.MongoClient

object NextCreateRouteStatesTool {
  def main(args: Array[String]): Unit = {
    val client = MongoClient("mongodb://localhost:27017")
    try {
      val mongoDatabase = client.getDatabase("kpn-next").withCodecRegistry(codecRegistry)
      val database = new NextDatabaseImpl(mongoDatabase)
      val lineSegmentTileCalculator = new LineSegmentTileCalculatorImpl(new TileCalculatorImpl())
      val tool = new NextCreateRouteStatesTool(database, lineSegmentTileCalculator)
      tool.createRelationStates()
    } finally {
      client.close()
    }
  }
}

class NextCreateRouteStatesTool(
  database: NextDatabase,
  lineSegmentTileCalculator: LineSegmentTileCalculator
) {

  private val log = Log(classOf[NextCreateRouteStatesTool])

  def createRelationStates(): Unit = {
    val routeIds = collectRouteIds()
    log.info(s"processing ${routeIds.size} route ids")
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      if (((index + 1) % 100) == 0) {
        log.info(s"${index + 1}/${routeIds.size}")
      }
      createRelationState(routeId)
    }
  }

  private def collectRouteIds(): Seq[Long] = {
    val allRouteIds = database.routeRelations.ids()
    val processedRouteIds = database.routeStates.ids()
    (allRouteIds.toSet -- processedRouteIds.toSet).toSeq.sorted
  }

  private def createRelationState(routeRelationId: Long): Unit = {
    database.routeRelations.findById(routeRelationId) match {
      case None => log.error(s"could find routeId $routeRelationId")
      case Some(relation) =>
        val elementIds = determineElementIds(relation.relation)
        val tiles = determineTiles(relation.relation)
        database.routeStates.save(
          NextRouteState(
            routeRelationId,
            tiles.map(_.name),
            elementIds
          )
        )
    }
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
        lineSegmentTileCalculator.tiles(z, lineSegments)
      }
    }.distinct.sortBy(tile => (tile.z, tile.x, tile.y))
  }
}
