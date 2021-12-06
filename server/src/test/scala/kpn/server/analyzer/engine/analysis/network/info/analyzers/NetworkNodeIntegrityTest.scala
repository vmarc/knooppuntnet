package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.core.util.UnitTest

class NetworkNodeIntegrityTest extends UnitTest {

  test("ok / notOk") {
    assert(!NetworkNodeIntegrity(0L, 3, 1).ok)
    assert(NetworkNodeIntegrity(0L, 3, 1).notOk)
    assert(NetworkNodeIntegrity(0L, 3, 3).ok)
    assert(!NetworkNodeIntegrity(0L, 3, 3).notOk)
  }
}
