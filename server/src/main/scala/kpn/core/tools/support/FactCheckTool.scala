package kpn.core.tools.support

import kpn.api.common.Fact
import kpn.api.custom.Subset
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.util.Mongo
import kpn.server.repository.FactRepositoryImpl
import kpn.server.repository.SubsetRepositoryImpl
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

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
    val pipeline = Seq(
      filter(
        and(
          equal("active", true),
          equal("labels", "fact-RouteWithoutWays"),
        )
      ),
      project(
        fields(
          include("_id")
        )
      )
    )
    val allRoutesWithoutWays = database.routes.aggregate[Id](pipeline)
    println("All routes without ways in the route collection (in any country):")
    allRoutesWithoutWays.foreach { id =>
      println(s"  ${id._id}")
    }
  }
}
