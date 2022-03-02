package kpn.core.tools.support

import kpn.api.common.ChangeSetSummary
import kpn.database.base.Database
import kpn.database.util.Mongo

object FixChangesTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-history") { database =>
      new FixChangesTool(database).fix()
    }
  }
}

class FixChangesTool(database: Database) {

  def fix(): Unit = {
    val ids = database.changes.stringIds()
    println(s"processing ${ids.size} changes")
    ids.zipWithIndex.foreach { case (id, index) =>
      if (((index + 1) % 100) == 0) {
        println(s"${index + 1}/${ids.size}")
      }
      database.changes.findByStringId(id).foreach(fixChangeSetSummary)
    }
  }

  private def fixChangeSetSummary(changeSetSummary: ChangeSetSummary): Unit = {
    val fixedChangeSetSummary = changeSetSummary.copy(
      locationChanges = changeSetSummary.locationChanges.map { locationChanges =>
        val happy = locationChanges.nodeChanges.happy || locationChanges.routeChanges.happy
        val investigate = locationChanges.nodeChanges.investigate || locationChanges.routeChanges.investigate
        locationChanges.copy(
          happy = happy,
          investigate = investigate
        )
      }
    )
    if (fixedChangeSetSummary != changeSetSummary) {
      println(s"update ${changeSetSummary.key.changeSetId}")
      database.changes.save(fixedChangeSetSummary)
    }
  }
}
