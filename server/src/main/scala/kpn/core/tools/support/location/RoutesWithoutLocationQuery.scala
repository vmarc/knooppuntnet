package kpn.core.tools.support.location

import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.size
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields

class RoutesWithoutLocationQuery(database: Database) {

  def execute(): Seq[RouteWithoutLocation] = {
    val pipeline = Seq(
      filter(
        size("analysis.locationAnalysis.locationNames", 0),
      ),
      project(
        fields(
          computed("name", "$summary.name"),
        )
      )
    )
    database.routes.aggregate[RouteWithoutLocation](pipeline)
  }
}
