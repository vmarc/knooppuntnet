package kpn.server.analyzer.engine.monitor.state

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.domain.TileCoordinate

class MonitorStateTileCoordinateSimplifierTest extends UnitTest {

  test("simplify coordinates - empty collection") {
    MonitorStateTileCoordinateSimplifier.simplify(Seq.empty) shouldBe Seq.empty
  }

  test("simplify coordinates") {
    assertEqual(
      MonitorStateTileCoordinateSimplifier.simplify(
        Seq(
          Seq(
            TileCoordinate(1, 1),
            TileCoordinate(2, 2),
          ),
          Seq(
            TileCoordinate(2, 2),
            TileCoordinate(3, 3),
          ),
          Seq(
            TileCoordinate(4, 4),
            TileCoordinate(5, 5),
          ),
          Seq(
            TileCoordinate(5, 5),
            TileCoordinate(6, 6),
          )
        )
      ),
      Seq(
        Seq(
          TileCoordinate(1, 1),
          TileCoordinate(2, 2),
          TileCoordinate(3, 3),
        ),
        Seq(
          TileCoordinate(4, 4),
          TileCoordinate(5, 5),
          TileCoordinate(6, 6),
        )
      )
    )
  }

  test("simplify coordinates - empty sequence") {
    assertEqual(
      MonitorStateTileCoordinateSimplifier.simplify(
        Seq(
          Seq(
            TileCoordinate(1, 1),
            TileCoordinate(2, 2),
          ),
          Seq(
            TileCoordinate(2, 2),
            TileCoordinate(3, 3),
          ),
          Seq.empty
        )
      ),
      Seq(
        Seq(
          TileCoordinate(1, 1),
          TileCoordinate(2, 2),
          TileCoordinate(3, 3),
        ),
      )
    )
  }
}
