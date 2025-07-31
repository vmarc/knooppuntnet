package kpn.server.analyzer.engine.monitor.state

import kpn.api.base.ObjectId
import kpn.core.test.TestObjects.newMonitorState
import kpn.core.test.TestObjects.newMonitorStateTile
import kpn.core.util.UnitTest
import kpn.server.monitor.domain.MonitorStateTile
import kpn.server.monitor.repository.MonitorRouteRepository
import org.scalamock.scalatest.MockFactory

class MonitorStateStoreTest extends UnitTest with MockFactory {

  test("save monitor route state and state tiles") {

    // setup

    val routeId: ObjectId = ObjectId("routeId")
    val relationId: Long = 1L

    val monitorState = newMonitorState(
      _id = ObjectId("state-id"),
      routeId = routeId,
      relationId = relationId,
    )

    val tile1 = newTile(1) // stays the same
    val tile2 = newTile(2) // will be updated
    val tile3 = newTile(3) // will be removed

    val tile2Updated = tile2.copy(_id = ObjectId("update-key"), matchesLines = Seq("match"))
    val tile4 = newTile(4) // new tile

    val oldTiles = Seq(
      tile1,
      tile2,
      tile3,
    )

    val newTiles = Seq(
      tile1,
      tile2Updated,
      tile4
    )

    val monitorRouteRepository = stub[MonitorRouteRepository]
    (monitorRouteRepository.stateTiles _).when(*, *).returns(oldTiles)

    val monitorStateTileBuilder = stub[MonitorStateTileBuilder]
    (monitorStateTileBuilder.build _).when(monitorState).returns(newTiles)

    val store = new MonitorStateStore(monitorRouteRepository, monitorStateTileBuilder)

    // execute
    store.saveState(monitorState)

    // verify
    // verify deleteTile calls
    (monitorRouteRepository.deleteStateTile _).verify(tile3._id)

    // verify save/update calls
    (monitorRouteRepository.saveState _).verify(monitorState)
    (monitorRouteRepository.saveStateTile _).verify(tile2Updated.copy(_id = tile2._id))
    (monitorRouteRepository.saveStateTile _).verify(tile4)
  }

  private def newTile(id: Int): MonitorStateTile = {
    newMonitorStateTile(_id = ObjectId(id.toString), routeId = ObjectId("routeId"), relationId = 1L, z = id, x = id, y = id)
  }
}
