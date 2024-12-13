package kpn.database.actions.routes

import kpn.api.common.NetworkType
import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.analyzer.engine.tiles.domain.TileId

class MongoQueryRouteTileNamesTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>
      val query = new MongoQueryRouteTileNames(database)

      database.routeTiles.save(buildTile(1, Seq(NetworkType.hiking), 2))
      database.routeTiles.save(buildTile(2, Seq(NetworkType.hiking), 2))
      database.routeTiles.save(buildTile(3, Seq(NetworkType.hiking), 2))
      database.routeTiles.save(buildTile(4, Seq(NetworkType.hiking), 1))
      database.routeTiles.save(buildTile(5, Seq(NetworkType.hiking, NetworkType.cycling), 1))

      query.execute(NetworkType.hiking) should equal(
        Seq(
          TileId(1, 1, 1),
          TileId(1, 1, 2),
        )
      )

      query.execute(NetworkType.cycling) should equal(
        Seq(
          TileId(1, 1, 1),
        )
      )
    }
  }

  private def buildTile(id: Long, networkTypes: Seq[NetworkType], y: Long): RouteTileDoc = {
    RouteTileDoc(
      _id = id.toString,
      routeId = id,
      routeName = id.toString,
      networkTypes = networkTypes,
      z = 1,
      x = 1,
      y = y,
      layer = "",
      scope = None,
      survey = None,
      error = None,
      segments = Seq.empty
    )
  }
}
