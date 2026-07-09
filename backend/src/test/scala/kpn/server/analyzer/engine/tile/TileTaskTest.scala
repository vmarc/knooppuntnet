package kpn.server.analyzer.engine.tile

import kpn.api.common.RouteType
import kpn.core.util.UnitTest

class TileTaskTest extends UnitTest {

  test("interprete cycling task") {

    val task = "tile-task:cycling-10-001-001"

    TileTask.routeType(task) should equal(RouteType.cycling)
    TileTask.tileName(task) should equal("10-001-001")
    TileTask.fullTileName(task) should equal("cycling-10-001-001")
    TileTask.zoomLevel(task) should equal(10)
  }

  test("interprete inline-skating task") {

    val task = "tile-task:inline-skating-10-001-001"

    TileTask.routeType(task) should equal(RouteType.inlineSkating)
    TileTask.tileName(task) should equal("10-001-001")
    TileTask.fullTileName(task) should equal("inline-skating-10-001-001")
    TileTask.zoomLevel(task) should equal(10)
  }

  test("routeType") {
    TileTask.routeType("tile-task:hiking-10-001-001") should equal(RouteType.hiking)
    TileTask.routeType("tile-task:cycling-10-001-001") should equal(RouteType.cycling)
    TileTask.routeType("tile-task:horse-riding-10-001-001") should equal(RouteType.horseRiding)
    TileTask.routeType("tile-task:canoe-10-001-001") should equal(RouteType.canoe)
    TileTask.routeType("tile-task:motorboat-10-001-001") should equal(RouteType.motorboat)
    TileTask.routeType("tile-task:inline-skating-10-001-001") should equal(RouteType.inlineSkating)
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
