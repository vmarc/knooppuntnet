package kpn.core.tools.support.location

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.size
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.fields
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter

class RoutesWithoutLocationQuery(database: Database) {

  def execute(): Seq[RouteWithoutLocation] = {
    val pipeline = Seq(
      filter(
        and(
          equal("active", true),
          size("analysis.locationAnalysis.locationNames", 0),
        )
      ),
      project(
        fields(
          computed("name", "$summary.name"),
        )
      )
    )
    database.routes.aggregate(pipeline, classOf[RouteWithoutLocation])
  }
}
