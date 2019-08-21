package kpn.core.db.views

import kpn.core.db.TestDocBuilder
import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.views.OrphanNodeView.OrphanNodeKey
import kpn.core.test.TestSupport.withDatabase
import kpn.shared.Country
import kpn.shared.NodeInfo
import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers

class OrphanNodeViewTest extends FunSuite with Matchers {

  test("orphan nodes are included in the view") {

    withDatabase { database =>

      val rows = node(database, Tags.from("rwn_ref" -> "01"), orphan = true, ignored = false)

      rows.map(_._1) should equal(
        Seq(
          OrphanNodeKey(ignored = false, orphan = true, display = true, "nl", "rwn", 10001)
        )
      )

      rows.map(_._2.id) should equal(
        Seq(
          10001
        )
      )
    }
  }

  test("nodes have both network types hiking and bicycle") {

    withDatabase { database =>

      val rows = node(database, Tags.from("rwn_ref" -> "01", "rcn_ref" -> "02"), orphan = true, ignored = false)

      rows.map(_._1) should equal(
        Seq(
          OrphanNodeKey(ignored = false, orphan = true, display = true, "nl", "rcn", 10001),
          OrphanNodeKey(ignored = false, orphan = true, display = true, "nl", "rwn", 10001)
        )
      )

      rows.map(_._2.id) should equal(
        Seq(
          10001,
          10001
        )
      )
    }
  }

  test("ignored nodes are included in the view") {

    withDatabase { database =>

      val rows = node(database, Tags.from("rwn_ref" -> "01"), orphan = true, ignored = true)

      rows.map(_._1) should equal(
        Seq(
          OrphanNodeKey(ignored = true, orphan = true, display = true, "nl", "rwn", 10001)
        )
      )

      rows.map(_._2.id) should equal(
        Seq(
          10001
        )
      )
    }
  }

  test("regular nodes are not included in the view") {
    withDatabase { database =>
      val rows = node(database, Tags.from("rwn_ref" -> "01"), orphan = false, ignored = false)
      rows should equal(Seq())
    }
  }

  test("inactive orphan nodes are not included in the view") {
    withDatabase { database =>
      val rows = node(database, Tags.from("rwn_ref" -> "01"), orphan = true, ignored = false, active = false)
      rows should equal(Seq())
    }
  }

  private def node(database: Database, tags: Tags, orphan: Boolean, ignored: Boolean, active: Boolean = true): Seq[(OrphanNodeKey, NodeInfo)] = {
    val b = new TestDocBuilder(database)
    b.node(10001, Country.nl, tags, orphan = orphan, ignored = ignored, active = active)
    database.query(AnalyzerDesign, OrphanNodeView, Couch.uiTimeout, stale = false)().map(OrphanNodeView.toKeyAndValue)
  }
}
