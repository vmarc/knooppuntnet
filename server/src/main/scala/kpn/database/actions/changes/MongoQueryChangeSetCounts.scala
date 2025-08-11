package kpn.database.actions.changes

import com.mongodb.client.model.Filters.and
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.actions.base.ChangeCountPipeline
import kpn.database.actions.changes.MongoQueryChangeSetCounts.log
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter

object MongoQueryChangeSetCounts {
  private val log = Log(classOf[MongoQueryChangeSetCounts])
}

class MongoQueryChangeSetCounts(database: Database) {

  def execute(subset: Option[Subset], year: Int, monthOption: Option[Int]): ChangeSetCounts = {

    val mainFilter = subset.map { subset =>
      filter(
        and(
          equal("subsets.country", subset.country.toString),
          equal("subsets.routeType", subset.routeType.toString)
        )
      )
    }

    ChangeCountPipeline.execute(
      database.changes,
      mainFilter.toSeq,
      year,
      monthOption,
      log
    )
  }
}
