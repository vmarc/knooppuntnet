package kpn.database.actions.nodes

import kpn.api.common.RouteType
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newBaseNodeDoc
import kpn.core.test.TestObjects.newNodeBaseData
import kpn.core.test.TestObjects.newNodeName
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileId

class MongoQueryNodeTileInfosTest extends MongoTest {

  test("execute") {

    database.baseNodes.save(
      newBaseNodeDoc(
        1001,
        base = newNodeBaseData(
          names = Seq(
            newNodeName(RouteType.hiking, name = "01")
          )
        ),
        tiles = Seq(
          "hiking-13-1-1",
          "hiking-14-1-1",
        )
      )
    )

    val query = new MongoQueryNodeTileInfos(database)

    assertEqual(
      query.byZoomLevel(RouteType.hiking, 13),
      Seq(
        NodeTileInfo(
          tileName = "13-1-1",
          nodeId = 1001,
          names = Seq(newNodeName(name = "01")),
          latitude = "0",
          longitude = "0",
          lastSurvey = None,
          tags = Seq.empty,
          facts = Seq.empty
        )
      )
    )

    assertEqual(
      query.byZoomLevel(RouteType.hiking, 14),
      Seq(
        NodeTileInfo(
          tileName = "14-1-1",
          nodeId = 1001,
          names = Seq(newNodeName(name = "01")),
          latitude = "0",
          longitude = "0",
          lastSurvey = None,
          tags = Seq.empty,
          facts = Seq.empty
        )
      )
    )

    assertEqual(
      query.byTileId(RouteType.hiking, TileId(13, 1, 1)),
      Seq(
        NodeTileInfo(
          tileName = "13-1-1",
          nodeId = 1001,
          names = Seq(newNodeName(name = "01")),
          latitude = "0",
          longitude = "0",
          lastSurvey = None,
          tags = Seq.empty,
          facts = Seq.empty
        )
      )
    )
  }
}
