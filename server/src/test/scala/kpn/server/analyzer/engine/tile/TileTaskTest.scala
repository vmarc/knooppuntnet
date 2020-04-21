package kpn.server.analyzer.engine.tile

import kpn.api.custom.NetworkType
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TileTaskTest extends AnyFunSuite with Matchers {

  test("interprete task") {

    val task = "tile-task:cycling-10-001-001"

    TileTask.networkType(task) should equal(NetworkType.cycling)
    TileTask.tileName(task) should equal("10-001-001")
    TileTask.fullTileName(task) should equal("cycling-10-001-001")
    TileTask.zoomLevel(task) should equal(10)
  }

}
