package kpn.server.analyzer.engine.changes.route.base

import kpn.core.util.UnitTest
import kpn.server.domain.StringCoordinate

class WayLineBuilderTest extends UnitTest {

  test("empty sequence") {
    WayLineBuilder.build(Seq.empty) shouldBe Seq.empty
  }

  test("single segment sequence") {
    val segments = Seq(
      segment("1", "1", "2", "2")
    )
    WayLineBuilder.build(segments) shouldBe Seq(
      Seq(
        StringCoordinate("1", "1"),
        StringCoordinate("2", "2"),
      )
    )
  }

  test("multiple segments") {
    val segments = Seq(
      segment("1", "1", "2", "2"),
      segment("4", "4", "3", "3"),
      segment("2", "2", "3", "3"),
    )
    WayLineBuilder.build(segments) shouldBe Seq(
      Seq(
        StringCoordinate("1", "1"),
        StringCoordinate("2", "2"),
        StringCoordinate("3", "3"),
        StringCoordinate("4", "4"),
      )
    )
  }

  test("multiple lines") {
    val segments = Seq(
      segment("1", "1", "2", "2"),
      segment("4", "4", "5", "5"),
      segment("2", "2", "3", "3"),
      segment("6", "6", "5", "5"),
    )
    WayLineBuilder.build(segments) shouldBe Seq(
      Seq(
        StringCoordinate("1", "1"),
        StringCoordinate("2", "2"),
        StringCoordinate("3", "3"),
      ),
      Seq(
        StringCoordinate("4", "4"),
        StringCoordinate("5", "5"),
        StringCoordinate("6", "6"),
      )
    )
  }

  private def segment(x1: String, y1: String, x2: String, y2: String): WaySegment = {
    WaySegment(StringCoordinate(x1, y1), StringCoordinate(x2, y2))
  }
}
