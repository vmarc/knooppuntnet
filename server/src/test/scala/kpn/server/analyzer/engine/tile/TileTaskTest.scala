package kpn.server.analyzer.engine.tile

import kpn.api.custom.NetworkType
import kpn.core.util.UnitTest

class TileTaskTest extends UnitTest {

  test("interprete cycling task") {

    val task = "tile-task:cycling-10-001-001"

    TileTask.networkType(task) should equal(NetworkType.cycling)
    TileTask.tileName(task) should equal("10-001-001")
    TileTask.fullTileName(task) should equal("cycling-10-001-001")
    TileTask.zoomLevel(task) should equal(10)
  }

  test("interprete inline-skating task") {

    val task = "tile-task:inline-skating-10-001-001"

    TileTask.networkType(task) should equal(NetworkType.inlineSkating)
    TileTask.tileName(task) should equal("10-001-001")
    TileTask.fullTileName(task) should equal("inline-skating-10-001-001")
    TileTask.zoomLevel(task) should equal(10)
  }

}
