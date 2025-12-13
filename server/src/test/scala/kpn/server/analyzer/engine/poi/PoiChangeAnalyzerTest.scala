package kpn.server.analyzer.engine.poi

import kpn.api.common.LatLonImpl
import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.poi.Poi
import kpn.api.custom.Change
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newPoi
import kpn.core.test.TestObjects.newRawNode
import kpn.core.test.TestObjects.newRawRelation
import kpn.core.test.TestObjects.newRawWay
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import kpn.server.analyzer.engine.changes.changes.OsmChange
import kpn.server.analyzer.engine.tile.PoiTileCalculator
import kpn.server.analyzer.engine.tiles.domain.PoiTiles
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.api.analysis.pages.poi.MasterPoiAnalyzer
import kpn.server.api.analysis.pages.poi.MasterPoiAnalyzerImpl
import kpn.server.repository.PoiRepository
import kpn.server.repository.TaskRepository
import kpn.server.repository.TaskRepositoryMock
import org.scalamock.stubs.Stub
import org.scalamock.stubs.Stubs

class PoiChangeAnalyzerTest extends UnitTest with Stubs {

  test("node poi add") {

    val t = new TestSetup()

    (t.knownPoiCache.contains _).returns {
      case PoiRef("node", 123) => false
      case _ => throw new IllegalArgumentException()
    }
    (t.poiScopeAnalyzer.inScope _).returnsWith(true)
    (t.poiTileCalculator.tileLonLat _).returns {
      case (13, _, _) => PoiTiles.tile(TileId(13, 0, 0))
      case (14, _, _) => PoiTiles.tile(TileId(14, 0, 0))
      case _ => throw new IllegalArgumentException()
    }
    (t.poiTileCalculator.poiTiles _).returnsWith(Seq("13-0-0", "14-0-0"))
    (t.poiRepository.get _).returns {
      case PoiRef("node", 123) => None
      case _ => throw new IllegalArgumentException()
    }

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Create,
            Seq(
              newRawNode(
                id = 123,
                latitude = "1",
                longitude = "2",
                tags = Tags.from("shop" -> "bicycle")
              )
            )
          )
        )
      )
    )

    val Seq(poi) = (t.poiRepository.save _).calls
    poi.elementType should equal("node")
    poi.elementId should equal(123)
    poi.latitude should equal("1")
    poi.longitude should equal("2")
    poi.layers should equal(Seq("bicycle"))
    poi.tags should equal(Tags.from("shop" -> "bicycle"))
    poi.tiles should equal(Seq("13-0-0", "14-0-0"))

    val Seq(poiRef) = (t.knownPoiCache.add _).calls
    poiRef.elementType should equal("node")
    poiRef.elementId should equal(123)

    t.taskRepository.all(PoiTileTask.prefix) should equal(
      Seq(
        "poi-tile-task:13-0-0",
        "poi-tile-task:14-0-0"
      )
    )
  }

  test("known node poi looses poi tags") {

    val t = new TestSetup()

    (t.poiRepository.get _).returns {
      case PoiRef("node", 123) => Some(existingPoi())
      case _ => throw new IllegalArgumentException()
    }
    (t.knownPoiCache.contains _).returns {
      case PoiRef("node", 123) => true
      case _ => throw new IllegalArgumentException()
    }

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Create,
            Seq(
              newRawNode(
                id = 123,
                latitude = "1",
                longitude = "2"
              )
            )
          )
        )
      )
    )

    val Seq(poiRef) = (t.poiRepository.delete _).calls
    poiRef.elementType should equal("node")
    poiRef.elementId should equal(123)

    val Seq(poiRef2) = (t.knownPoiCache.delete _).calls
    poiRef2.elementType should equal("node")
    poiRef2.elementId should equal(123)

    t.taskRepository.all(PoiTileTask.prefix) should equal(
      Seq(
        "poi-tile-task:13-0-0",
        "poi-tile-task:14-0-0"
      )
    )
  }

  test("unknown node poi update") {

    val t = new TestSetup()

    (t.knownPoiCache.contains _).returns {
      case PoiRef("node", 123) => false
      case _ => throw new IllegalArgumentException()
    }
    (t.poiScopeAnalyzer.inScope _).returnsWith(true)
    (t.poiTileCalculator.tileLonLat _).returns {
      case (13, _, _) => PoiTiles.tile(TileId(13, 0, 0))
      case (14, _, _) => PoiTiles.tile(TileId(14, 0, 0))
      case _ => throw new IllegalArgumentException()
    }
    (t.poiTileCalculator.poiTiles _).returnsWith(Seq("13-0-0", "14-0-0"))
    (t.poiRepository.get _).returns {
      case PoiRef("node", 123) => None
      case _ => throw new IllegalArgumentException()
    }

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Modify,
            Seq(
              newRawNode(
                id = 123,
                latitude = "1",
                longitude = "2",
                tags = Tags.from("shop" -> "bicycle")
              )
            )
          )
        )
      )
    )

    val Seq(poi) = (t.poiRepository.save _).calls
    poi.elementType should equal("node")
    poi.elementId should equal(123)
    poi.latitude should equal("1")
    poi.longitude should equal("2")
    poi.layers should equal(Seq("bicycle"))
    poi.tags should equal(Tags.from("shop" -> "bicycle"))
    poi.tiles should equal(Seq("13-0-0", "14-0-0"))

    val Seq(poiRef) = (t.knownPoiCache.add _).calls
    poiRef.elementType should equal("node")
    poiRef.elementId should equal(123)

    t.taskRepository.all(PoiTileTask.prefix) should equal(
      Seq(
        "poi-tile-task:13-0-0",
        "poi-tile-task:14-0-0"
      )
    )
  }

  test("known node poi update with tile change") {

    val t = new TestSetup()

    (t.poiRepository.get _).returns {
      case PoiRef("node", 123) => Some(existingPoi())
      case _ => throw new IllegalArgumentException()
    }
    (t.knownPoiCache.contains _).returns {
      case PoiRef("node", 123) => true
      case _ => throw new IllegalArgumentException()
    }
    (t.poiScopeAnalyzer.inScope _).returnsWith(true)
    (t.poiTileCalculator.tileLonLat _).returns {
      case (13, _, _) => PoiTiles.tile(TileId(13, 1, 1))
      case (14, _, _) => PoiTiles.tile(TileId(14, 1, 1))
    }
    (t.poiTileCalculator.poiTiles _).returnsWith(Seq("13-1-1", "14-1-1"))

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Modify,
            Seq(
              newRawNode(
                id = 123,
                latitude = "1",
                longitude = "2",
                tags = Tags.from("shop" -> "bicycle")
              )
            )
          )
        )
      )
    )

    val Seq(poi) = (t.poiRepository.save _).calls
    poi.elementType should equal("node")
    poi.elementId should equal(123)
    poi.latitude should equal("1")
    poi.longitude should equal("2")
    poi.layers should equal(Seq("bicycle"))
    poi.tags should equal(Tags.from("shop" -> "bicycle"))
    poi.tiles should equal(Seq("13-1-1", "14-1-1"))

    val Seq(poiRef) = (t.knownPoiCache.add _).calls
    poiRef.elementType should equal("node")
    poiRef.elementId should equal(123)

    t.taskRepository.all(PoiTileTask.prefix) should equal(
      Seq(
        "poi-tile-task:13-0-0",
        "poi-tile-task:13-1-1",
        "poi-tile-task:14-0-0",
        "poi-tile-task:14-1-1"
      )
    )
  }

  test("known node poi delete") {

    val t = new TestSetup()

    (t.poiRepository.get _).returns {
      case PoiRef("node", 123) => Some(existingPoi())
      case _ => throw new IllegalArgumentException()
    }
    (t.knownPoiCache.contains _).returns {
      case PoiRef("node", 123) => true
      case _ => throw new IllegalArgumentException()
    }

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Delete,
            Seq(
              newRawNode(
                id = 123,
                latitude = "1",
                longitude = "2"
              )
            )
          )
        )
      )
    )

    val Seq(poiRef) = (t.poiRepository.delete _).calls
    poiRef.elementType should equal("node")
    poiRef.elementId should equal(123)

    val Seq(poiRef2) = (t.knownPoiCache.delete _).calls
    poiRef2.elementType should equal("node")
    poiRef2.elementId should equal(123)

    t.taskRepository.all(PoiTileTask.prefix) should equal(Seq("poi-tile-task:13-0-0", "poi-tile-task:14-0-0"))
  }

  test("unknown non-poi node delete") { // unlikely case

    val t = new TestSetup()

    (t.poiRepository.get _).returns {
      case PoiRef("node", 123) => Some(existingPoi())
      case _ => throw new IllegalArgumentException()
    }
    (t.knownPoiCache.contains _).returns {
      case PoiRef("node", 123) => false
      case _ => throw new IllegalArgumentException()
    }

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Delete,
            Seq(
              newRawNode(
                id = 123,
                latitude = "1",
                longitude = "2"
              )
            )
          )
        )
      )
    )

    val Seq(poiRef) = (t.poiRepository.delete _).calls
    poiRef.elementType should equal("node")
    poiRef.elementId should equal(123)

    val Seq(poiRef2) = (t.knownPoiCache.delete _).calls
    poiRef.elementType should equal("node")
    poiRef.elementId should equal(123)

    t.taskRepository.all(PoiTileTask.prefix) should equal(Seq("poi-tile-task:13-0-0", "poi-tile-task:14-0-0"))
  }

  test("unknown non-poi node (but existing in database) delete") {

    val t = new TestSetup()

    (t.poiRepository.get _).returns {
      case PoiRef("node", 123) => None
      case _ => throw new IllegalArgumentException()
    }
    (t.knownPoiCache.contains _).returns {
      case PoiRef("node", 123) => false
      case _ => throw new IllegalArgumentException()
    }

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Delete,
            Seq(
              newRawNode(
                id = 123,
                latitude = "1",
                longitude = "2"
              )
            )
          )
        )
      )
    )

    (t.poiRepository.delete _).times should equal(0)

    val Seq(poiRef) = (t.knownPoiCache.delete _).calls
    poiRef.elementType should equal("node")
    poiRef.elementId should equal(123)

    t.taskRepository.all(PoiTileTask.prefix) shouldBe empty
  }

  test("known node poi not in scope anymore") {

    val t = new TestSetup()

    (t.poiRepository.get _).returns {
      case PoiRef("node", 123) => Some(existingPoi())
      case _ => throw new IllegalArgumentException()
    }
    (t.knownPoiCache.contains _).returns {
      case PoiRef("node", 123) => true
      case _ => throw new IllegalArgumentException()
    }
    (t.poiScopeAnalyzer.inScope _).returnsWith(false)

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Modify,
            Seq(
              newRawNode(
                id = 123,
                latitude = "1",
                longitude = "2",
                tags = Tags.from("shop" -> "bicycle")
              )
            )
          )
        )
      )
    )

    val Seq(poiRef) = (t.poiRepository.delete _).calls
    poiRef.elementType should equal("node")
    poiRef.elementId should equal(123)

    val Seq(poiRef2) = (t.knownPoiCache.delete _).calls
    poiRef2.elementType should equal("node")
    poiRef2.elementId should equal(123)

    t.taskRepository.all(PoiTileTask.prefix) should equal(
      Seq(
        "poi-tile-task:13-0-0",
        "poi-tile-task:14-0-0"
      )
    )
  }

  test("unknown node poi not in scope: no action") {

    val t = new TestSetup()

    (t.poiRepository.get _).returns {
      case PoiRef("node", 123) => None
      case _ => throw new IllegalArgumentException()
    }
    (t.knownPoiCache.contains _).returns {
      case PoiRef("node", 123) => false
      case _ => throw new IllegalArgumentException()
    }
    (t.poiScopeAnalyzer.inScope _).returnsWith(false)

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Modify,
            Seq(
              newRawNode(
                id = 123,
                latitude = "1",
                longitude = "2",
                tags = Tags.from("shop" -> "bicycle")
              )
            )
          )
        )
      )
    )

    (t.poiRepository.save _).times should equal(0)
    (t.poiRepository.delete _).times should equal(0)
    (t.knownPoiCache.add _).times should equal(0)
    (t.knownPoiCache.delete _).times should equal(0)
    t.taskRepository.all(PoiTileTask.prefix) shouldBe empty
  }

  test("way poi") {

    val t = new TestSetup()

    (t.knownPoiCache.contains _).returns {
      case PoiRef("way", 123) => false
      case _ => throw new IllegalArgumentException()
    }
    (t.poiScopeAnalyzer.inScope _).returnsWith(true)
    (t.poiTileCalculator.tileLonLat _).returns {
      case (13, _, _) => PoiTiles.tile(TileId(13, 1, 1))
      case (14, _, _) => PoiTiles.tile(TileId(14, 1, 1))
      case _ => throw new IllegalArgumentException()
    }
    (t.poiTileCalculator.poiTiles _).returnsWith(Seq("13-0-0", "14-0-0"))
    (t.poiRepository.get _).returns {
      case PoiRef("way", 123) => None
      case _ => throw new IllegalArgumentException()
    }
    (t.poiQueryExecutor.centers _).returns {
      case ("way", Seq(123L)) =>
        Seq(
          ElementCenter(123, LatLonImpl("1", "2"))
        )
      case _ => throw new IllegalArgumentException()
    }

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Create,
            Seq(
              newRawWay(
                id = 123,
                tags = Tags.from("shop" -> "bicycle")
              )
            )
          )
        )
      )
    )

    val Seq(poi) = (t.poiRepository.save _).calls
    poi.elementType should equal("way")
    poi.elementId should equal(123)
    poi.latitude should equal("1")
    poi.longitude should equal("2")
    poi.layers should equal(Seq("bicycle"))
    poi.tags should equal(Tags.from("shop" -> "bicycle"))
    poi.tiles should equal(Seq("13-0-0", "14-0-0"))

    val Seq(poiRef) = (t.knownPoiCache.add _).calls
    poiRef.elementType should equal("way")
    poiRef.elementId should equal(123)

    t.taskRepository.all(PoiTileTask.prefix) should equal(
      Seq(
        "poi-tile-task:13-0-0",
        "poi-tile-task:14-0-0"
      )
    )
  }

  test("way poi - not found in overpass database") {

    val t = new TestSetup()

    (t.knownPoiCache.contains _).returns {
      case PoiRef("way", 123) => false
      case _ => throw new IllegalArgumentException()
    }
    (t.poiRepository.get _).returns {
      case PoiRef("way", 123) => None
      case _ => throw new IllegalArgumentException()
    }
    (t.poiQueryExecutor.centers _).returns { case ("way", Seq(123L)) => Seq.empty }

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Modify,
            Seq(
              newRawWay(
                id = 123,
                tags = Tags.from("shop" -> "bicycle")
              )
            )
          )
        )
      )
    )

    val Seq(poiRef) = (t.knownPoiCache.delete _).calls
    poiRef.elementType should equal("way")
    poiRef.elementId should equal(123)

    (t.poiRepository.save _).times should equal(0)
    (t.poiRepository.delete _).times should equal(0)
    (t.knownPoiCache.add _).times should equal(0)
    t.taskRepository.all(PoiTileTask.prefix) shouldBe empty
  }

  test("relation poi") {

    val t = new TestSetup()

    (t.knownPoiCache.contains _).returns {
      case PoiRef("relation", 123) => false
      case _ => throw new IllegalArgumentException()
    }
    (t.poiScopeAnalyzer.inScope _).returnsWith(true)
    (t.poiTileCalculator.tileLonLat _).returns {
      case (13, _, _) => PoiTiles.tile(TileId(13, 0, 0))
      case (14, _, _) => PoiTiles.tile(TileId(14, 0, 0))
    }
    (t.poiTileCalculator.poiTiles _).returnsWith(Seq("13-0-0", "14-0-0"))
    (t.poiRepository.get _).returns {
      case PoiRef("relation", 123) => None
      case _ => throw new IllegalArgumentException()
    }
    (t.poiQueryExecutor.centers _).returns { case ("relation", Seq(123L)) =>
      Seq(
        ElementCenter(123, LatLonImpl("1", "2"))
      )
    }

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Create,
            Seq(
              newRawRelation(
                id = 123,
                tags = Tags.from("shop" -> "bicycle")
              )
            )
          )
        )
      )
    )

    val Seq(poi) = (t.poiRepository.save _).calls
    poi.elementType should equal("relation")
    poi.elementId should equal(123)
    poi.latitude should equal("1")
    poi.longitude should equal("2")
    poi.layers should equal(Seq("bicycle"))
    poi.tags should equal(Tags.from("shop" -> "bicycle"))
    poi.tiles should equal(Seq("13-0-0", "14-0-0"))

    val Seq(poiRef) = (t.knownPoiCache.add _).calls
    poiRef.elementType should equal("relation")
    poiRef.elementId should equal(123)

    t.taskRepository.all(PoiTileTask.prefix) should equal(
      Seq(
        "poi-tile-task:13-0-0",
        "poi-tile-task:14-0-0"
      )
    )
  }

  private def existingPoi(): Poi = {
    newPoi(
      elementType = "node",
      elementId = 123,
      latitude = "1",
      longitude = "2",
      tags = Tags.from("shop" -> "bicycle"),
      layers = Seq("bicycle"),
      tiles = Seq("13-0-0", "14-0-0")
    )
  }

  private class TestSetup {

    val poiRepository: Stub[PoiRepository] = stub[PoiRepository]
    (poiRepository.save _).returnsWith(())
    (poiRepository.delete _).returnsWith(())
    val knownPoiCache: Stub[KnownPoiCache] = stub[KnownPoiCache]
    (knownPoiCache.add _).returnsWith(())
    (knownPoiCache.delete _).returnsWith(())
    val poiTileCalculator: Stub[PoiTileCalculator] = stub[PoiTileCalculator]
    val taskRepository: TaskRepository = new TaskRepositoryMock()
    val poiQueryExecutor: Stub[PoiQueryExecutor] = stub[PoiQueryExecutor]
    val poiScopeAnalyzer: Stub[PoiScopeAnalyzer] = stub[PoiScopeAnalyzer]
    val locationAnalyzer: Stub[LocationAnalyzer] = stub[LocationAnalyzer]
    (locationAnalyzer.findLocations _).returnsWith(Seq.empty)
    val masterPoiAnalyzer: MasterPoiAnalyzer = new MasterPoiAnalyzerImpl()

    val poiChangeAnalyzer: PoiChangeAnalyzer = new PoiChangeAnalyzer(
      analyzerPoiUpdateEnabled = true,
      knownPoiCache,
      poiRepository,
      poiTileCalculator,
      taskRepository,
      poiScopeAnalyzer,
      poiQueryExecutor,
      locationAnalyzer,
      masterPoiAnalyzer
    )
  }
}
