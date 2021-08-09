package kpn.server.analyzer.engine.changes.data

import kpn.core.util.UnitTest

class OrphanNodesDataTest extends UnitTest {

  test("test methods") {

    val data = new NodesData()

    data.size should equal(0)
    assert(!data.contains(1001))
    data.ids shouldBe empty
    data.delete(1001) // does no fail

    data.add(1001)

    data.size should equal(1)
    data.contains(1001) should equal(true)
    data.ids should equal(Set(1001))
    data.delete(1001)

    data.size should equal(0)
  }

}
