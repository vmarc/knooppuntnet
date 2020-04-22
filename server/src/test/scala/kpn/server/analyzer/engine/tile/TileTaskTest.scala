package kpn.server.analyzer.engine.tile

import kpn.api.custom.NetworkType
import kpn.core.util.UnitTest

class TileTaskTest extends UnitTest {

  test("interprete task") {

    val task = "tile-task:cycling-10-001-001"

    TileTask.networkType(task) should equal(NetworkType.cycling)
    TileTask.tileName(task) should equal("10-001-001")
    TileTask.fullTileName(task) should equal("cycling-10-001-001")
    TileTask.zoomLevel(task) should equal(10)
  }

}
