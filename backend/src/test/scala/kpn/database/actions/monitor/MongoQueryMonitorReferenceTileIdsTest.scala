package kpn.database.actions.monitor

import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newMonitorReference
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.monitor.domain.MonitorReferenceTile
import kpn.server.monitor.repository.MonitorRouteRepository
import org.bson.types.ObjectId

class MongoQueryMonitorReferenceTileIdsTest extends MongoTest {

  test("all reference tile ids") {

    setupReferences()

    val tileIds = new MongoQueryMonitorReferenceTileIds(database).execute()

    assertEqual(
      tileIds,
      Seq(
        TileId(11, 12, 13),
        TileId(21, 22, 23),
        TileId(31, 32, 33),
      )
    )
  }

  private def setupReferences(): Unit = {

    val monitorRouteRepository = new MonitorRouteRepository(database)

    monitorRouteRepository.saveReference(
      newMonitorReference(
        routeId = ObjectId.get(),
        relationId = None,
        tiles = Seq(
          MonitorReferenceTile(
            z = 11,
            x = 12,
            y = 13,
            lines = Seq.empty
          )
        )
      )
    )

    monitorRouteRepository.saveReference(
      newMonitorReference(
        routeId = ObjectId.get(),
        relationId = None,
        tiles = Seq(
          MonitorReferenceTile(
            z = 21,
            x = 22,
            y = 23,
            lines = Seq.empty
          ),
          MonitorReferenceTile(
            z = 31,
            x = 32,
            y = 33,
            lines = Seq.empty
          )
        )
      )
    )
  }
}
