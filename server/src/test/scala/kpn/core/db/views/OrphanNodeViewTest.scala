package kpn.core.db.views

import kpn.core.db.TestDocBuilder
import kpn.core.db.couch.Couch
import kpn.core.db.couch.OldDatabase
import kpn.core.db.views.OrphanNodeView.OrphanNodeKey
import kpn.core.test.TestSupport.withDatabase
import kpn.shared.Country
import kpn.shared.NetworkType
import kpn.shared.NodeInfo
import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers

class OrphanNodeViewTest extends FunSuite with Matchers {

  test("orphan nodes are included in the view") {
    doOrphanNodeTest(NetworkType.bicycle)
    doOrphanNodeTest(NetworkType.hiking)
    doOrphanNodeTest(NetworkType.horseRiding)
    doOrphanNodeTest(NetworkType.motorboat)
    doOrphanNodeTest(NetworkType.canoe)
    doOrphanNodeTest(NetworkType.inlineSkates)
  }

  private def doOrphanNodeTest(networkType: NetworkType): Unit = {
    withDatabase { database =>
      val rows = node(database, Tags.from(networkType.nodeTagKey -> "01"), orphan = true)
      rows.map(_._1) should equal(
        Seq(
          OrphanNodeKey(orphan = true, "nl", networkType.name, 10001)
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

      val rows = node(database, Tags.from("rwn_ref" -> "01", "rcn_ref" -> "02"), orphan = true)

      rows.map(_._1) should equal(
        Seq(
          OrphanNodeKey(orphan = true, "nl", "rcn", 10001),
          OrphanNodeKey(orphan = true, "nl", "rwn", 10001)
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

  test("regular nodes are not included in the view") {
    withDatabase { database =>
      val rows = node(database, Tags.from("rwn_ref" -> "01"), orphan = false)
      rows should equal(Seq())
    }
  }

  test("inactive orphan nodes are not included in the view") {
    withDatabase { database =>
      val rows = node(database, Tags.from("rwn_ref" -> "01"), orphan = true, active = false)
      rows should equal(Seq())
    }
  }

  private def node(database: OldDatabase, tags: Tags, orphan: Boolean, active: Boolean = true): Seq[(OrphanNodeKey, NodeInfo)] = {
    val b = new TestDocBuilder(database)
    b.node(10001, Country.nl, tags, orphan = orphan, active = active)
    database.query(AnalyzerDesign, OrphanNodeView, Couch.uiTimeout, stale = false)().map(OrphanNodeView.toKeyAndValue)
  }
}
