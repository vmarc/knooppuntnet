package kpn.core.mongo.actions.subsets

import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.Subset
import kpn.core.mongo.Database
import kpn.core.mongo.actions.subsets.MongoQuerySubsetNetworks.log
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

object MongoQuerySubsetNetworks extends MongoQuery {
  private val log = Log(classOf[MongoQuerySubsetNetworks])
}

class MongoQuerySubsetNetworks(database: Database) {

  def execute(subset: Subset): Seq[NetworkAttributes] = {

    val pipeline = Seq(
      filter(
        and(
          // equal("labels", "active"),
          // equal("labels", s"location-${subset.country.domain}"),
          // equal("labels", s"network-type-${subset.networkType.name}")
          equal("active", true),
          equal("attributes.country", subset.country.domain),
          equal("attributes.networkType", subset.networkType.name)
        )
      ),
      sort(orderBy(ascending("attributes.name"))),
      project(
        fields(
          excludeId(),
          include("attributes")
        )
      )
    )

    log.debugElapsed {
      val networks = database.networks.aggregate[NetworkInfo](pipeline, log)
      val result = s"subset ${subset.name} networks: ${networks.size}"
      (result, networks.map(_.attributes))
    }
  }
}
