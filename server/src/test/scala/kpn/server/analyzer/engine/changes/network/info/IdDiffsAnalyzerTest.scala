package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.diff.IdDiffs
import kpn.core.util.UnitTest

class IdDiffsAnalyzerTest extends UnitTest {

  test("IdDiffs") {
    IdDiffsAnalyzer.analyze(Seq.empty, Seq.empty) should equal(IdDiffs())
    IdDiffsAnalyzer.analyze(Seq(1L), Seq.empty) should equal(IdDiffs(removed = Seq(1L)))
    IdDiffsAnalyzer.analyze(Seq.empty, Seq(1L)) should equal(IdDiffs(added = Seq(1L)))
    IdDiffsAnalyzer.analyze(Seq(1L, 2L), Seq(1L, 3L)) should equal(IdDiffs(removed = Seq(2L), added = Seq(3L)))
  }
}
