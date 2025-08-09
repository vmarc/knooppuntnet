package kpn.core.tools.support

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.Fact
import kpn.api.custom.Subset
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo
import kpn.server.repository.FactRepositoryImpl
import kpn.server.repository.SubsetRepositoryImpl

object FactCheckTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      val tool = new FactCheckTool(database)
      tool.statisticsBasedQuery()
      tool.factsPerNetworkQuery()
      tool.routesCollectionBasedQuery()
    }
  }
}

class FactCheckTool(database: Database) {
  private val subsetRepository = new SubsetRepositoryImpl(database)
  private val factRepository = new FactRepositoryImpl(database)

  def statisticsBasedQuery(): Unit = {
    val factCounts = subsetRepository.subsetFactCounts(Subset.frHiking)
    val factCount = factCounts.filter(_.fact == Fact.RouteWithoutWays).head
    println(s"Statistics based factCount: ${factCount.count}")
  }

  def factsPerNetworkQuery(): Unit = {
    val networkFactRefss = factRepository.factsPerNetwork(Subset.frHiking, Fact.RouteWithoutWays)
    println(networkFactRefss)
  }

  def routesCollectionBasedQuery(): Unit = {
    val pipeline: MongoPipeline = Seq(
      filter(
        and(
          Filters.eq("active", true),
          Filters.eq("labels", "fact-RouteWithoutWays"),
        )
      ),
      project(
        fields(
          include("_id")
        )
      )
    )
    val allRoutesWithoutWays = database.routes.aggregate(pipeline, classOf[Id])
    println("All routes without ways in the route collection (in any country):")
    allRoutesWithoutWays.foreach { id =>
      println(s"  ${id._id}")
    }
  }
}
