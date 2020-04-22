package kpn.server.analyzer.engine.changes.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RouteChange
import kpn.api.custom.Fact
import kpn.core.util.UnitTest

class OrphanRouteChangeTest extends UnitTest with SharedTestObjects {

  test("a regular route change is not an orphan route related change") {
    OrphanRouteChange.isOrphanRouteChange(newRouteChange()) should equal(false)
  }

  test("create orphan route") {
    assertOrphan(newRouteChange(changeType = ChangeType.Create, facts = Seq(Fact.OrphanRoute)))
  }

  test("update orphan route") {
    assertOrphan(newRouteChange(changeType = ChangeType.Update, facts = Seq(Fact.OrphanRoute)))
  }

  test("update route that was orphan") {
    assertOrphan(newRouteChange(changeType = ChangeType.Update, facts = Seq(Fact.WasOrphan, Fact.LostRouteTags)))
  }

  test("delete orphan route") {
    assertOrphan(newRouteChange(changeType = ChangeType.Delete, facts = Seq(Fact.OrphanRoute)))
  }

  test("delete route that was orphan") {
    assertOrphan(newRouteChange(changeType = ChangeType.Delete, facts = Seq(Fact.WasOrphan)))
  }

  private def assertOrphan(routeChange: RouteChange): Unit = {
    OrphanRouteChange.isOrphanRouteChange(routeChange) should equal(true)
  }
}
