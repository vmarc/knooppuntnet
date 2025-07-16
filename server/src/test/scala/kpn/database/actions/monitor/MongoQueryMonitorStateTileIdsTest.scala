package kpn.database.actions.monitor

import kpn.api.base.ObjectId
import kpn.core.test.MongoTest
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.monitor.domain.MonitorStateTile
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl

class MongoQueryMonitorStateTileIdsTest extends MongoTest {

  test("all state tile ids") {

    setupStateTiles()

    val tileIds = new MongoQueryMonitorStateTileIds(database).execute()
    assertEqual(
      tileIds,
      Seq(
        TileId(11, 12, 13),
        TileId(21, 22, 23),
      )
    )
  }

  private def setupStateTiles(): Unit = {

    val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)

    monitorRouteRepository.saveStateTile(
      MonitorStateTile(
        _id = ObjectId(),
        routeId = ObjectId(),
        relationId = 1,
        z = 11,
        x = 12,
        y = 13,
        deviations = Seq.empty,
        matchesLines = Seq.empty
      )
    )

    monitorRouteRepository.saveStateTile(
      MonitorStateTile(
        _id = ObjectId(),
        routeId = ObjectId(),
        relationId = 1,
        z = 21,
        x = 22,
        y = 23,
        deviations = Seq.empty,
        matchesLines = Seq.empty
      )
    )
  }
}
