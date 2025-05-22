package kpn.database.actions.routes

import kpn.api.common.search.ConditionGroup
import kpn.core.util.Log
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

object MongoQueryRoutes {
  private val log = Log(classOf[MongoQueryRoutes])
}

class MongoQueryRoutes(database: Database) {

  def execute(group: ConditionGroup, log: Log = MongoQueryRoutes.log): Seq[Long] = {
    log.debugElapsed {

      val pipeline = Seq(
        filter(
          and(
            equal("active", true),
            SearchQueryBuilder.buildFilter(group)
          )
        ),
        sort(
          orderBy(
            ascending(
              "summary.name",
            )
          )
        ),
        project(
          fields(
            include("_id"),
            computed("tags", "$summary.tags")
          )
        )
      )
      val results = database.routes.aggregate[SearchQueryResult](pipeline, log, allowDiskUse = true)
      val ids = SearchQueryPostProcessor.process(group, results).map(_._id)
      (s"${ids.size} routes", ids)
    }
  }
}
