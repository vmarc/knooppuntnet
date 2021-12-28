package kpn.database.actions.changes

import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.actions.base.ChangeCountPipeline
import kpn.database.actions.changes.MongoQueryChangeSetCounts.log
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal

object MongoQueryChangeSetCounts {
  private val log = Log(classOf[MongoQueryChangeSetCounts])
}

class MongoQueryChangeSetCounts(database: Database) {

  def execute(subset: Option[Subset], year: Int, monthOption: Option[Int]): ChangeSetCounts = {

    val mainFilter = subset.map { subset =>
      filter(
        and(
          equal("subsets.country", subset.country.domain),
          equal("subsets.networkType", subset.networkType.name)
        )
      )
    }

    ChangeCountPipeline.execute(
      database.changes,
      mainFilter,
      year,
      monthOption,
      log
    )
  }
}
