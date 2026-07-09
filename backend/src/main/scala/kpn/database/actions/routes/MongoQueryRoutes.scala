package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.search.ConditionGroup
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQueryRoutes {
  private val log = Log(classOf[MongoQueryRoutes])
}

class MongoQueryRoutes(database: Database) {

  def execute(group: ConditionGroup, log: Log = MongoQueryRoutes.log): Seq[Long] = {
    log.debugElapsed {
      val pipeline = buildPipeline(group)
      val results = database.routes.aggregate(pipeline, classOf[SearchQueryResult], log, allowDiskUse = true)
      val ids = SearchQueryPostProcessor.process(group, results).map(_._id)
      (s"${ids.size} routes", ids)
    }
  }

  private def buildPipeline(group: ConditionGroup): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          SearchQueryBuilder.buildFilter(group)
        )
      ),
      sort(
        orderBy(
          ascending(
            "base.name",
          )
        )
      ),
      project(
        fields(
          include("_id"),
          computed("tags", "$base.raw.tags")
        )
      )
    )
  }
}
