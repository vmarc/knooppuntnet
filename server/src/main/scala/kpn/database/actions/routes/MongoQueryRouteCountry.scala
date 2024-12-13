package kpn.database.actions.routes

import kpn.api.common.Country
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryRouteCountry.log
import kpn.database.base.CountryResult
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.lookup
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.elemMatch
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRouteCountry {
  private val log = Log(classOf[MongoQueryRouteCountry])
}

class MongoQueryRouteCountry(database: Database) {
  def execute(routeId: Long): Option[Country] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("active", true),
            elemMatch("relationMembers", equal("relationId", routeId))
          )
        ),
        lookup("network-infos", "_id", "_id", "networkInfos"),
        filter(
          elemMatch("networkInfos", equal("active", true))
        ),
        project(
          fields(
            computed("country", "$networkInfos.country")
          )
        ),
        unwind("$country"),
        limit(1),
        project(
          fields(
            excludeId(),
            include("country")
          )
        ),
      )
      val country = database.networks.optionAggregate[CountryResult](pipeline, log).map(_.country)
      (s"route $routeId country: $country", country)
    }
  }
}
