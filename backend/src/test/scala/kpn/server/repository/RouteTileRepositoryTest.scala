package kpn.server.repository

import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newRouteTileInfo

class RouteTileRepositoryTest extends MongoTest {

  test("find route tile ids") {

    val routeTileRepository = new RouteTileRepository(database)

    routeTileRepository.saveRouteTile(newRouteTileInfo("tile-1", 11))
    routeTileRepository.saveRouteTile(newRouteTileInfo("tile-2", 11))

    assertEqual(
      routeTileRepository.routeTileIds(11),
      Seq("tile-1", "tile-2")
    )
  }

  test("delete route tiles") {

    val routeTileRepository = new RouteTileRepository(database)

    routeTileRepository.saveRouteTile(newRouteTileInfo("tile-1", 11))
    routeTileRepository.saveRouteTile(newRouteTileInfo("tile-2", 11))

    assertEqual(
      routeTileRepository.routeTiles(11).map(_._id),
      Seq("tile-1", "tile-2")
    )

    routeTileRepository.deleteRouteTiles(11)

    routeTileRepository.routeTiles(11) shouldBe empty
  }
}
