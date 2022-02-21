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

  test("networkType") {
    TileTask.networkType("tile-task:hiking-10-001-001") should equal(NetworkType.hiking)
    TileTask.networkType("tile-task:cycling-10-001-001") should equal(NetworkType.cycling)
    TileTask.networkType("tile-task:horse-riding-10-001-001") should equal(NetworkType.horseRiding)
    TileTask.networkType("tile-task:canoe-10-001-001") should equal(NetworkType.canoe)
    TileTask.networkType("tile-task:motorboat-10-001-001") should equal(NetworkType.motorboat)
    TileTask.networkType("tile-task:inline-skating-10-001-001") should equal(NetworkType.inlineSkating)
  }

  test("zoomLevel") {
    TileTask.zoomLevel("tile-task:hiking-11-001-001") should equal(11)
    TileTask.zoomLevel("tile-task:cycling-12-001-001") should equal(12)
    TileTask.zoomLevel("tile-task:horse-riding-13-001-001") should equal(13)
    TileTask.zoomLevel("tile-task:canoe-14-001-001") should equal(14)
    TileTask.zoomLevel("tile-task:motorboat-15-001-001") should equal(15)
    TileTask.zoomLevel("tile-task:inline-skating-16-001-001") should equal(16)
  }

  test("task") {
    TileTask.task("hiking-11-001-001") should equal("tile-task:hiking-11-001-001")
  }
}
