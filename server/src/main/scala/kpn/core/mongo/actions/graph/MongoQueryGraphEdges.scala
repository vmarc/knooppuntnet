package kpn.core.mongo.actions.graph

import kpn.core.mongo.Database
import kpn.core.mongo.GraphEdges
import kpn.core.mongo.actions.graph.MongoQueryGraphEdges.log
import kpn.core.mongo.util.Mongo
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.model.Accumulators.push
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryGraphEdges extends MongoQuery {
  private val log = Log(classOf[MongoQueryGraphEdges])

  def main(args: Array[String]): Unit = {
    println("MongoQueryGraphEdges")
    Mongo.executeIn("kpn-test") { database =>
      val query = new MongoQueryGraphEdges(database)
      database.nodes.findById(0L)
      query.execute()
    }
  }
}

class MongoQueryGraphEdges(database: Database) {

  def execute(): Seq[GraphEdges] = {

    val pipeline = Seq(
      unwind("$edges"),
      group(
        "$networkType",
        push("edges", "$edges")
      ),
      project(
        fields(
          excludeId(),
          computed("networkType", "$_id"),
          include("edges")
        )
      )
    )

    log.debugElapsed {
      val edges = database.routeEdges.aggregate[GraphEdges](pipeline, log)
      val result = edges.sortBy(_.edges.size).reverse.map(e => s"${e.networkType.name}: ${e.edges.size}").mkString(", ")
      (result, edges)
    }
  }
}
