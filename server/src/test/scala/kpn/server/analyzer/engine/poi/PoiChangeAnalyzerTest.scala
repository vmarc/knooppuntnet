package kpn.server.analyzer.engine.poi

import kpn.api.common.LatLonImpl
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.poi.Poi
import kpn.api.custom.Change
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import kpn.server.analyzer.engine.changes.changes.OsmChange
import kpn.server.analyzer.engine.tile.TileCalculator
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.api.analysis.pages.poi.MasterPoiAnalyzer
import kpn.server.api.analysis.pages.poi.MasterPoiAnalyzerImpl
import kpn.server.repository.MockTaskRepository
import kpn.server.repository.PoiRepository
import kpn.server.repository.TaskRepository

class PoiChangeAnalyzerTest extends UnitTest with SharedTestObjects {

  test("node poi add") {

    val t = new TestSetup()

    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(false)
    (t.poiScopeAnalyzer.inScope _).when(*).returns(true)
    (t.tileCalculator.tileLonLat _).when(13, *, *).returns(new Tile(13, 0, 0))
    (t.tileCalculator.tileLonLat _).when(14, *, *).returns(new Tile(14, 0, 0))
    (t.tileCalculator.poiTiles _).when(*, *).returns(Seq("13-0-0", "14-0-0"))
    (t.poiRepository.get _).when(PoiRef("node", 123)).returns(None)

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Create,
            Seq(
              newRawNode(
                id = 123L,
                latitude = "1",
                longitude = "2",
                tags = Tags.from("shop" -> "bicycle")
              )
            )
          )
        )
      )
    )

    (t.poiRepository.save _).verify(
      where { poi: Poi =>
        poi.elementType should equal("node")
        poi.elementId should equal(123)
        poi.latitude should equal("1")
        poi.longitude should equal("2")
        poi.layers should equal(Seq("bicycle"))
        poi.tags should equal(Tags.from("shop" -> "bicycle"))
        poi.tiles should equal(Seq("13-0-0", "14-0-0"))
        true
      }
    ).once()

    (t.knownPoiCache.add _).verify(
      where { poiRef: PoiRef =>
        poiRef.elementType should equal("node")
        poiRef.elementId should equal(123)
        true
      }
    ).once()

    t.taskRepository.all(PoiTileTask.prefix) should equal(
      Seq(
        "poi-tile-task:13-0-0",
        "poi-tile-task:14-0-0"
      )
    )
  }

  test("known node poi looses poi tags") {

    val t = new TestSetup()

    (t.poiRepository.get _).when(PoiRef("node", 123)).returns(Some(existingPoi()))
    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(true)

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Create,
            Seq(
              newRawNode(
                id = 123L,
                latitude = "1",
                longitude = "2"
              )
            )
          )
        )
      )
    )

    (t.poiRepository.delete _).verify(
      where { poiRef: PoiRef =>
        poiRef.elementType should equal("node")
        poiRef.elementId should equal(123)
        true
      }
    ).once()

    (t.knownPoiCache.delete _).verify(
      where { poiRef: PoiRef =>
        poiRef.elementType should equal("node")
        poiRef.elementId should equal(123)
        true
      }
    ).once()

    t.taskRepository.all(PoiTileTask.prefix) should equal(
      Seq(
        "poi-tile-task:13-0-0",
        "poi-tile-task:14-0-0"
      )
    )
  }

  test("unknown node poi update") {

    val t = new TestSetup()

    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(false)
    (t.poiScopeAnalyzer.inScope _).when(*).returns(true)
    (t.tileCalculator.tileLonLat _).when(13, *, *).returns(new Tile(13, 0, 0))
    (t.tileCalculator.tileLonLat _).when(14, *, *).returns(new Tile(14, 0, 0))
    (t.tileCalculator.poiTiles _).when(*, *).returns(Seq("13-0-0", "14-0-0"))
    (t.poiRepository.get _).when(PoiRef("node", 123)).returns(None)

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Modify,
            Seq(
              newRawNode(
                id = 123L,
                latitude = "1",
                longitude = "2",
                tags = Tags.from("shop" -> "bicycle")
              )
            )
          )
        )
      )
    )

    (t.poiRepository.save _).verify(
      where { poi: Poi =>
        poi.elementType should equal("node")
        poi.elementId should equal(123)
        poi.latitude should equal("1")
        poi.longitude should equal("2")
        poi.layers should equal(Seq("bicycle"))
        poi.tags should equal(Tags.from("shop" -> "bicycle"))
        poi.tiles should equal(Seq("13-0-0", "14-0-0"))
        true
      }
    ).once()

    (t.knownPoiCache.add _).verify(
      where { poiRef: PoiRef =>
        poiRef.elementType should equal("node")
        poiRef.elementId should equal(123)
        true
      }
    ).once()

    t.taskRepository.all(PoiTileTask.prefix) should equal(
      Seq(
        "poi-tile-task:13-0-0",
        "poi-tile-task:14-0-0"
      )
    )
  }

  test("known node poi update with tile change") {

    val t = new TestSetup()

    (t.poiRepository.get _).when(PoiRef("node", 123)).returns(Some(existingPoi()))
    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(false)
    (t.poiScopeAnalyzer.inScope _).when(*).returns(true)
    (t.tileCalculator.tileLonLat _).when(13, *, *).returns(new Tile(13, 1, 1))
    (t.tileCalculator.tileLonLat _).when(14, *, *).returns(new Tile(14, 1, 1))
    (t.tileCalculator.poiTiles _).when(*, *).returns(Seq("13-1-1", "14-1-1"))
    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(true)

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Modify,
            Seq(
              newRawNode(
                id = 123L,
                latitude = "1",
                longitude = "2",
                tags = Tags.from("shop" -> "bicycle")
              )
            )
          )
        )
      )
    )

    (t.poiRepository.save _).verify(
      where { poi: Poi =>
        poi.elementType should equal("node")
        poi.elementId should equal(123)
        poi.latitude should equal("1")
        poi.longitude should equal("2")
        poi.layers should equal(Seq("bicycle"))
        poi.tags should equal(Tags.from("shop" -> "bicycle"))
        poi.tiles should equal(Seq("13-1-1", "14-1-1"))
        true
      }
    ).once()

    (t.knownPoiCache.add _).verify(
      where { poiRef: PoiRef =>
        poiRef.elementType should equal("node")
        poiRef.elementId should equal(123)
        true
      }
    ).once()

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

    (t.poiRepository.get _).when(PoiRef("node", 123)).returns(Some(existingPoi()))
    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(true)

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Delete,
            Seq(
              newRawNode(
                id = 123L,
                latitude = "1",
                longitude = "2"
              )
            )
          )
        )
      )
    )

    (t.poiRepository.delete _).verify(
      where { poiRef: PoiRef =>
        poiRef.elementType should equal("node")
        poiRef.elementId should equal(123)
        true
      }
    ).once()

    (t.knownPoiCache.delete _).verify(
      where { poiRef: PoiRef =>
        poiRef.elementType should equal("node")
        poiRef.elementId should equal(123)
        true
      }
    ).once()

    t.taskRepository.all(PoiTileTask.prefix) should equal(Seq("poi-tile-task:13-0-0", "poi-tile-task:14-0-0"))
  }

  test("unknown non-poi node delete") { // unlikely case

    val t = new TestSetup()

    (t.poiRepository.get _).when(PoiRef("node", 123)).returns(Some(existingPoi()))
    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(false)

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Delete,
            Seq(
              newRawNode(
                id = 123L,
                latitude = "1",
                longitude = "2"
              )
            )
          )
        )
      )
    )

    (t.poiRepository.delete _).verify(
      where { poiRef: PoiRef =>
        poiRef.elementType should equal("node")
        poiRef.elementId should equal(123)
        true
      }
    ).once()

    (t.knownPoiCache.delete _).verify(
      where { poiRef: PoiRef =>
        poiRef.elementType should equal("node")
        poiRef.elementId should equal(123)
        true
      }
    ).once()

    t.taskRepository.all(PoiTileTask.prefix) should equal(Seq("poi-tile-task:13-0-0", "poi-tile-task:14-0-0"))
  }

  test("unknown non-poi node (but existing in database) delete") {

    val t = new TestSetup()

    (t.poiRepository.get _).when(PoiRef("node", 123)).returns(None)
    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(false)

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Delete,
            Seq(
              newRawNode(
                id = 123L,
                latitude = "1",
                longitude = "2"
              )
            )
          )
        )
      )
    )

    (t.poiRepository.delete _).verify(*).never()

    (t.knownPoiCache.delete _).verify(
      where { poiRef: PoiRef =>
        poiRef.elementType should equal("node")
        poiRef.elementId should equal(123)
        true
      }
    ).once()

    t.taskRepository.all(PoiTileTask.prefix) shouldBe empty
  }

  test("known node poi not in scope anymore") {

    val t = new TestSetup()

    (t.poiRepository.get _).when(PoiRef("node", 123)).returns(Some(existingPoi()))
    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(true)
    (t.poiScopeAnalyzer.inScope _).when(*).returns(false)

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Modify,
            Seq(
              newRawNode(
                id = 123L,
                latitude = "1",
                longitude = "2",
                tags = Tags.from("shop" -> "bicycle")
              )
            )
          )
        )
      )
    )

    (t.poiRepository.delete _).verify(
      where { poiRef: PoiRef =>
        poiRef.elementType should equal("node")
        poiRef.elementId should equal(123)
        true
      }
    ).once()

    (t.knownPoiCache.delete _).verify(
      where { poiRef: PoiRef =>
        poiRef.elementType should equal("node")
        poiRef.elementId should equal(123)
        true
      }
    ).once()

    t.taskRepository.all(PoiTileTask.prefix) should equal(Seq("poi-tile-task:13-0-0", "poi-tile-task:14-0-0"))
  }

  test("unknown node poi not in scope: no action") {

    val t = new TestSetup()

    (t.poiRepository.get _).when(PoiRef("node", 123)).returns(None)
    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(false)
    (t.poiScopeAnalyzer.inScope _).when(*).returns(false)

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Modify,
            Seq(
              newRawNode(
                id = 123L,
                latitude = "1",
                longitude = "2",
                tags = Tags.from("shop" -> "bicycle")
              )
            )
          )
        )
      )
    )

    (t.poiRepository.save _).verify(*).never()
    (t.poiRepository.delete _).verify(*).never()
    (t.knownPoiCache.add _).verify(*).never()
    (t.knownPoiCache.delete _).verify(*).never()
    t.taskRepository.all(PoiTileTask.prefix) shouldBe empty
  }

  test("way poi") {

    val t = new TestSetup()

    (t.knownPoiCache.contains _).when(PoiRef("way", 123)).returns(false)
    (t.poiScopeAnalyzer.inScope _).when(*).returns(true)
    (t.tileCalculator.tileLonLat _).when(13, *, *).returns(new Tile(13, 0, 0))
    (t.tileCalculator.tileLonLat _).when(14, *, *).returns(new Tile(14, 0, 0))
    (t.tileCalculator.poiTiles _).when(*, *).returns(Seq("13-0-0", "14-0-0"))
    (t.poiRepository.get _).when(PoiRef("way", 123)).returns(None)
    (t.poiQueryExecutor.centers _).when("way", Seq(123L)).returns(
      Seq(
        ElementCenter(123L, LatLonImpl("1", "2"))
      )
    )

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Create,
            Seq(
              newRawWay(
                id = 123L,
                tags = Tags.from("shop" -> "bicycle")
              )
            )
          )
        )
      )
    )

    (t.poiRepository.save _).verify(
      where { poi: Poi =>
        poi.elementType should equal("way")
        poi.elementId should equal(123)
        poi.latitude should equal("1")
        poi.longitude should equal("2")
        poi.layers should equal(Seq("bicycle"))
        poi.tags should equal(Tags.from("shop" -> "bicycle"))
        poi.tiles should equal(Seq("13-0-0", "14-0-0"))
        true
      }
    ).once()

    (t.knownPoiCache.add _).verify(
      where { poiRef: PoiRef =>
        poiRef.elementType should equal("way")
        poiRef.elementId should equal(123)
        true
      }
    ).once()

    t.taskRepository.all(PoiTileTask.prefix) should equal(
      Seq(
        "poi-tile-task:13-0-0",
        "poi-tile-task:14-0-0"
      )
    )
  }

  test("way poi - not found in overpass database") {

    val t = new TestSetup()

    (t.knownPoiCache.contains _).when(PoiRef("way", 123L)).returns(false)
    (t.poiRepository.get _).when(PoiRef("way", 123L)).returns(None)
    (t.poiQueryExecutor.centers _).when("way", Seq(123L)).returns(Seq.empty)

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Modify,
            Seq(
              newRawWay(
                id = 123L,
                tags = Tags.from("shop" -> "bicycle")
              )
            )
          )
        )
      )
    )

    (t.knownPoiCache.delete _).verify(
      where { poiRef: PoiRef =>
        poiRef.elementType should equal("way")
        poiRef.elementId should equal(123)
        true
      }
    ).once()

    (t.poiRepository.save _).verify(*).never()
    (t.poiRepository.delete _).verify(*).never()
    (t.knownPoiCache.add _).verify(*).never()
    t.taskRepository.all(PoiTileTask.prefix) shouldBe empty
  }

  test("relation poi") {

    val t = new TestSetup()

    (t.knownPoiCache.contains _).when(PoiRef("relation", 123)).returns(false)
    (t.poiScopeAnalyzer.inScope _).when(*).returns(true)
    (t.tileCalculator.tileLonLat _).when(13, *, *).returns(new Tile(13, 0, 0))
    (t.tileCalculator.tileLonLat _).when(14, *, *).returns(new Tile(14, 0, 0))
    (t.tileCalculator.poiTiles _).when(*, *).returns(Seq("13-0-0", "14-0-0"))
    (t.poiRepository.get _).when(PoiRef("relation", 123)).returns(None)
    (t.poiQueryExecutor.centers _).when("relation", Seq(123L)).returns(
      Seq(
        ElementCenter(123, LatLonImpl("1", "2"))
      )
    )

    t.poiChangeAnalyzer.analyze(
      OsmChange(
        Seq(
          Change(
            Create,
            Seq(
              newRawRelation(
                id = 123L,
                tags = Tags.from("shop" -> "bicycle")
              )
            )
          )
        )
      )
    )

    (t.poiRepository.save _).verify(
      where { poi: Poi =>
        poi.elementType should equal("relation")
        poi.elementId should equal(123)
        poi.latitude should equal("1")
        poi.longitude should equal("2")
        poi.layers should equal(Seq("bicycle"))
        poi.tags should equal(Tags.from("shop" -> "bicycle"))
        poi.tiles should equal(Seq("13-0-0", "14-0-0"))
        true
      }
    ).once()

    (t.knownPoiCache.add _).verify(
      where { poiRef: PoiRef =>
        poiRef.elementType should equal("relation")
        poiRef.elementId should equal(123)
        true
      }
    ).once()

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
      elementId = 123L,
      latitude = "1",
      longitude = "2",
      tags = Tags.from("shop" -> "bicycle"),
      layers = Seq("bicycle"),
      tiles = Seq("13-0-0", "14-0-0")
    )
  }

  private class TestSetup {

    val poiRepository: PoiRepository = stub[PoiRepository]
    val knownPoiCache: KnownPoiCache = stub[KnownPoiCache]
    val tileCalculator: TileCalculator = stub[TileCalculator]
    val taskRepository: TaskRepository = new MockTaskRepository()
    val poiQueryExecutor: PoiQueryExecutor = stub[PoiQueryExecutor]
    val poiScopeAnalyzer: PoiScopeAnalyzer = stub[PoiScopeAnalyzer]
    val locationAnalyzer: LocationAnalyzer = stub[LocationAnalyzer]
    val masterPoiAnalyzer: MasterPoiAnalyzer = new MasterPoiAnalyzerImpl()

    val poiChangeAnalyzer: PoiChangeAnalyzer = new PoiChangeAnalyzerImpl(
      knownPoiCache,
      poiRepository,
      tileCalculator,
      taskRepository,
      poiScopeAnalyzer,
      poiQueryExecutor,
      locationAnalyzer,
      masterPoiAnalyzer
    )
  }
}
