package kpn.core.database.views.analyzer

import kpn.api.custom.Country
import kpn.api.custom.NetworkScope
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.TestObjects
import kpn.core.db.TestDocBuilder
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class OrphanNodeViewTest extends UnitTest with TestObjects {

  test("orphan nodes are included in the view") {
    doOrphanNodeTest(Subset.nlBicycle)
    doOrphanNodeTest(Subset.nlHiking)
    doOrphanNodeTest(Subset.nlHorseRiding)
    doOrphanNodeTest(Subset.nlMotorboat)
    doOrphanNodeTest(Subset.nlCanoe)
    doOrphanNodeTest(Subset.nlInlineSkates)
  }

  private def doOrphanNodeTest(subset: Subset): Unit = {
    withDatabase { database =>
      val b = new TestDocBuilder(database)
      val nodeTagKey = ScopedNetworkType(NetworkScope.regional, subset.networkType).nodeTagKey
      b.node(1001, Country.nl, tags = Tags.from(nodeTagKey -> "01"), orphan = true)

      val nodeInfos = OrphanNodeView.query(database, subset, stale = false)
      nodeInfos.map(_.id) should equal(Seq(1001))
    }
  }

  test("nodes have both network types hiking and bicycle") {

    withDatabase { database =>
      val b = new TestDocBuilder(database)
      b.node(1001, Country.nl, tags = Tags.from("rwn_ref" -> "01", "rcn_ref" -> "02"), orphan = true)

      val hikingNodeInfos = OrphanNodeView.query(database, Subset.nlHiking, stale = false)
      hikingNodeInfos.map(_.id) should equal(Seq(1001))

      val bicycleNodeInfos = OrphanNodeView.query(database, Subset.nlBicycle, stale = false)
      bicycleNodeInfos.map(_.id) should equal(Seq(1001))
    }
  }

  test("regular nodes are not included in the view") {
    withDatabase { database =>
      val b = new TestDocBuilder(database)
      b.node(1001, Country.nl, tags = Tags.from("rwn_ref" -> "01"))
      val nodeInfos = OrphanNodeView.query(database, Subset.nlHiking, stale = false)
      nodeInfos should equal(Seq.empty)
    }
  }

  test("inactive orphan nodes are not included in the view") {
    withDatabase { database =>
      val b = new TestDocBuilder(database)
      b.node(1001, Country.nl, tags = Tags.from("rwn_ref" -> "01"), orphan = true, active = false)
      val nodeInfos = OrphanNodeView.query(database, Subset.nlHiking, stale = false)
      nodeInfos should equal(Seq.empty)
    }
  }

}
