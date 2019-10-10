package kpn.core.engine.changes.orphan.node

import kpn.shared.Fact
import kpn.shared.SharedTestObjects
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NodeChange
import org.scalatest.FunSuite
import org.scalatest.Matchers

class OrphanNodeChangeTest extends FunSuite with Matchers with SharedTestObjects {

  test("a regular node change is not an orphan node related change") {
    OrphanNodeChange.isOrphanNodeChange(newNodeChange()) should equal(false)
  }

  test("create orphan node") {
    assertOrphan(newNodeChange(changeType = ChangeType.Create, facts = Seq(Fact.OrphanNode)))
  }

  test("update orphan node") {
    assertOrphan(newNodeChange(changeType = ChangeType.Update, facts = Seq(Fact.OrphanNode)))
  }

  test("update node that was orphan") {
    assertOrphan(newNodeChange(changeType = ChangeType.Update, facts = Seq(Fact.WasOrphan, Fact.LostHikingNodeTag)))
    assertOrphan(newNodeChange(changeType = ChangeType.Update, facts = Seq(Fact.WasOrphan, Fact.LostBicycleNodeTag)))
  }

  test("delete orphan node") {
    assertOrphan(newNodeChange(changeType = ChangeType.Delete, facts = Seq(Fact.OrphanNode)))
  }

  test("delete node that was orphan") {
    assertOrphan(newNodeChange(changeType = ChangeType.Delete, facts = Seq(Fact.WasOrphan)))
  }

  private def assertOrphan(nodeChange: NodeChange): Unit = {
    OrphanNodeChange.isOrphanNodeChange(nodeChange) should equal(true)
  }
}

