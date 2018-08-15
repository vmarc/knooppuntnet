package kpn.core.engine.changes.route

import kpn.shared.Fact
import kpn.shared.SharedTestObjects
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.RouteChange
import org.scalatest.FunSuite
import org.scalatest.Matchers

class OrphanRouteChangeTest extends FunSuite with Matchers with SharedTestObjects {

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
