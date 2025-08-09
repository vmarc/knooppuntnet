package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.limit
import com.mongodb.client.model.Aggregates.lookup
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.elemMatch
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.Country
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryRouteCountry.log
import kpn.database.base.CountryResult
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQueryRouteCountry {
  private val log = Log(classOf[MongoQueryRouteCountry])
}

class MongoQueryRouteCountry(database: Database) {
  def execute(routeId: Long): Option[Country] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeId)
      val country = database.baseNetworks.optionAggregate(pipeline, classOf[CountryResult], log).map(_.country)
      (s"route $routeId country: $country", country)
    }
  }

  private def buildPipeline(routeId: Long): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
        )
      ),
      unwind("$members"),
      filter(
        and(
          equal("members.memberType", "relation"),
          equal("members.ref", routeId),
        )
      ),
      lookup("networks", "_id", "_id", "networks"),
      filter(
        elemMatch("networks", equal("active", true))
      ),
      project(
        fields(
          computed("country", "$networks.country")
        )
      ),
      unwind("$country"),
      limit(1),
      project(
        fields(
          excludeId(),
          include("country")
        )
      )
    )
  }
}
