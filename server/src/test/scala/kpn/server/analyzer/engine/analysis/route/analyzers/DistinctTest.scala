package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.core.util.UnitTest

class DistinctTest extends UnitTest {

  test("Seq.distinct retains the sorting order") {
    Seq(1, 1, 1, 1, 2, 2, 3, 1, 1, 3).distinct should equal(Seq(1, 2, 3))
    Seq(2, 2, 3, 1, 1, 1, 1, 3, 1, 1, 3).distinct should equal(Seq(2, 3, 1))
  }
}
