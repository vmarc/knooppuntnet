package kpn.database.actions.changes

import kpn.core.util.Log
import kpn.database.actions.base.ChangeCountPipeline
import kpn.database.actions.changes.MongoQueryChangeSetCounts.log
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.Database

object MongoQueryChangeSetCounts {
  private val log = Log(classOf[MongoQueryChangeSetCounts])
}

class MongoQueryChangeSetCounts(database: Database) {

  def execute(year: Int, monthOption: Option[Int]): ChangeSetCounts = {
    ChangeCountPipeline.execute(
      database.changes,
      None,
      year,
      monthOption,
      log
    )
  }
}
