package kpn.core.tools.monitor

import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.route.update.MonitorRouteRelationRepository
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

case class RelationId(relationId: Long)

object MonitorSymbolTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-monitor") { database =>
      new MonitorSymbolTool(database).report()
    }
  }
}

class MonitorSymbolTool(database: Database) {

  private val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
  private val monitorRouteRelationRepository = new MonitorRouteRelationRepository(overpassQueryExecutor)

  def report(): Unit = {
    val pipeline = Seq(
      project(
        fields(
          include("relationId"),
        )
      )
    )

    val relationIds = database.monitorRouteStates.aggregate[RelationId](pipeline).map(_.relationId).sorted.distinct

    val results = relationIds.zipWithIndex.flatMap { case (relationId, index) =>
      println(s"${index + 1}/${relationIds.size} $relationId")
      monitorRouteRelationRepository.loadTopLevel(None, relationId).flatMap { relation =>
        relation.tags("osmc:symbol").map { value =>
          val result = value -> relationId
          println(result)
          result
        }
      }
    }
    results.sorted.foreach { case x => println(s"['${x._1}', '${x._2}'],") }
  }
}
