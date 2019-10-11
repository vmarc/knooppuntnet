package kpn.core.db.views

import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.server.repository.ChangeSetRepositoryImpl
import kpn.core.test.TestSupport.withDatabase
import kpn.shared.ChangeSetSubsetAnalysis
import kpn.shared.ChangeSetSummary
import kpn.shared.NetworkChanges
import kpn.shared.Subset
import kpn.shared.Timestamp
import kpn.shared.changes.details.ChangeKey
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ChangesViewTest extends FunSuite with Matchers {

  private val timestamp = Timestamp(2015, 8, 11, 0, 0, 0)

  test("no data") {
    withDatabase { database =>
      val rows = database.query(ChangesDesign, ChangesView, Couch.uiTimeout, stale = false)()
      rows.size should equal(0)
    }
  }

  test("changeset without impact") {
    withDatabase { database =>

      val changeSetSummary = newChangeSetSummary()

      viewResult(database, changeSetSummary) should equal(
        Seq(
          """{"id":"change:123:1:summary:0","key":["change-set","2015","08","11","00:00:00","123","1"],"value":[1,0]}"""
        )
      )
    }
  }

  test("changeset impact happy") {
    withDatabase { database =>

      val changeSetSummary = newChangeSetSummary().copy(happy = true)

      viewResult(database, changeSetSummary) should equal(
        Seq(
          """{"id":"change:123:1:summary:0","key":["change-set","2015","08","11","00:00:00","123","1"],"value":[1,1]}""",
          """{"id":"change:123:1:summary:0","key":["impacted:change-set","2015","08","11","00:00:00","123","1"],"value":[1,1]}"""
        )
      )
    }
  }

  test("changeset impact investigate") {
    withDatabase { database =>

      val changeSetSummary = newChangeSetSummary().copy(happy = true)

      viewResult(database, changeSetSummary) should equal(
        Seq(
          """{"id":"change:123:1:summary:0","key":["change-set","2015","08","11","00:00:00","123","1"],"value":[1,1]}""",
          """{"id":"change:123:1:summary:0","key":["impacted:change-set","2015","08","11","00:00:00","123","1"],"value":[1,1]}"""
        )
      )
    }
  }

  test("changeset subset no impact") {
    withDatabase() { database =>

      val changeSetSummary = newChangeSetSummary().copy(subsets = Seq(Subset.nlHiking), subsetAnalyses = Seq(ChangeSetSubsetAnalysis(Subset.nlHiking)))

      viewResult(database, changeSetSummary) should equal(
        Seq(
          """{"id":"change:123:1:summary:0","key":["change-set","2015","08","11","00:00:00","123","1"],"value":[1,0]}""",
          """{"id":"change:123:1:summary:0","key":["nl:rwn:change-set","2015","08","11","00:00:00","123","1"],"value":[1,0]}"""
        )
      )
    }
  }

  test("changeset subset happy") {
    withDatabase() { database =>

      val changeSetSummary = newChangeSetSummary().copy(
        happy = true,
        subsets = Seq(
          Subset.nlHiking
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(Subset.nlHiking, happy = true)
        )
      )

      viewResult(database, changeSetSummary) should equal(
        Seq(
          """{"id":"change:123:1:summary:0","key":["change-set","2015","08","11","00:00:00","123","1"],"value":[1,1]}""",
          """{"id":"change:123:1:summary:0","key":["impacted:change-set","2015","08","11","00:00:00","123","1"],"value":[1,1]}""",
          """{"id":"change:123:1:summary:0","key":["nl:rwn:change-set","2015","08","11","00:00:00","123","1"],"value":[1,1]}""",
          """{"id":"change:123:1:summary:0","key":["nl:rwn:impacted:change-set","2015","08","11","00:00:00","123","1"],"value":[1,1]}"""
        )
      )
    }
  }

  private def newChangeSetSummary(): ChangeSetSummary = {
    ChangeSetSummary(
      key = ChangeKey(1, timestamp, 123, 0),
      subsets = Seq.empty,
      timestampFrom = timestamp,
      timestampUntil = timestamp,
      networkChanges = NetworkChanges(),
      orphanRouteChanges = Seq.empty,
      orphanNodeChanges = Seq.empty,
      subsetAnalyses = Seq()
    )
  }

  private def viewResult(database: Database, changeSetSummary: ChangeSetSummary): Seq[String] = {
    val repo = new ChangeSetRepositoryImpl(database)
    repo.saveChangeSetSummary(changeSetSummary)
    val rows = database.query(ChangesDesign, ChangesView, Couch.uiTimeout, stale = false)()
    // rows.foreach { row => println("\"\"\"" + row.toString + "\"\"\",") }
    rows.map(_.toString)
  }

}
