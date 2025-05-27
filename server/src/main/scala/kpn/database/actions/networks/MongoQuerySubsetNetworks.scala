package kpn.database.actions.networks

import kpn.api.custom.Subset
import kpn.core.doc.NetworkDoc
import kpn.core.util.Log
import kpn.database.actions.networks.MongoQuerySubsetNetworks.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

object MongoQuerySubsetNetworks {
  private val log = Log(classOf[MongoQuerySubsetNetworks])
}

class MongoQuerySubsetNetworks(database: Database) {

  def execute(subset: Subset): Seq[NetworkDoc] = {
    val pipeline = buildPipeline(subset)
    log.debugElapsed {
      val networks = database.networks.aggregate[NetworkDoc](pipeline, log)
      val result = s"subset ${subset.name} networks: ${networks.size}"
      (result, networks)
    }
  }

  private def buildPipeline(subset: Subset): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("country", subset.country.entryName),
          equal("summary.routeType", subset.routeType.entryName)
        )
      ),
      sort(orderBy(ascending("summary.name"))),
      project(
        fields(
          include("country"),
          include("summary"),
          include("detail"),
        )
      )
    )
  }
}
