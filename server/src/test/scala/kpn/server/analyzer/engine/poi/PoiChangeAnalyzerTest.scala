package kpn.server.analyzer.engine.poi

import kpn.api.common.Poi
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Change
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.changes.changes.OsmChange
import kpn.server.analyzer.engine.tile.TileCalculator
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.repository.MockTaskRepository
import kpn.server.repository.PoiRepository
import kpn.server.repository.TaskRepository
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

class PoiChangeAnalyzerTest extends FunSuite with Matchers with SharedTestObjects with MockFactory {

  test("node poi add") {

    val t = new TestSetup()

    val node = newRawNode(
      id = 123L,
      latitude = "1",
      longitude = "2",
      tags = Tags.from("shop" -> "bicycle")
    )

    val osmChange = OsmChange(Seq(Change.create(node)))

    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(false)
    (t.poiScopeAnalyzer.inScope _).when(*).returns(true)
    (t.tileCalculator.tileLonLat _).when(13, *, *).returns(new Tile(13, 0, 0))
    (t.tileCalculator.tileLonLat _).when(14, *, *).returns(new Tile(14, 0, 0))

    t.poiChangeAnalyzer.analyze(osmChange)

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

    val node = newRawNode(
      id = 123L,
      latitude = "1",
      longitude = "2"
    )

    val osmChange = OsmChange(Seq(Change.create(node)))

    val existingPoi = newPoi(
      elementType = "node",
      elementId = 123L,
      latitude = "1",
      longitude = "2",
      tags = Tags.from("shop" -> "bicycle"),
      layers = Seq("bicycle"),
      tiles = Seq("13-0-0", "14-0-0")
    )

    (t.poiRepository.poi _).when(PoiRef("node", 123)).returns(Some(existingPoi))

    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(true)

    t.poiChangeAnalyzer.analyze(osmChange)

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

    val node = newRawNode(
      id = 123L,
      latitude = "1",
      longitude = "2",
      tags = Tags.from("shop" -> "bicycle")
    )

    val osmChange = OsmChange(Seq(Change.modify(node)))

    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(false)
    (t.poiScopeAnalyzer.inScope _).when(*).returns(true)
    (t.tileCalculator.tileLonLat _).when(13, *, *).returns(new Tile(13, 0, 0))
    (t.tileCalculator.tileLonLat _).when(14, *, *).returns(new Tile(14, 0, 0))

    t.poiChangeAnalyzer.analyze(osmChange)

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

    val node = newRawNode(
      id = 123L,
      latitude = "1",
      longitude = "2"
    )

    val osmChange = OsmChange(Seq(Change.modify(node)))

    val existingPoi = newPoi(
      elementType = "node",
      elementId = 123L,
      latitude = "1",
      longitude = "2",
      tags = Tags.from("shop" -> "bicycle"),
      layers = Seq("bicycle"),
      tiles = Seq("13-0-0", "14-0-0")
    )

    (t.poiRepository.poi _).when(PoiRef("node", 123)).returns(Some(existingPoi))

    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(false)
    (t.poiScopeAnalyzer.inScope _).when(*).returns(true)
    (t.tileCalculator.tileLonLat _).when(13, *, *).returns(new Tile(13, 1, 1))
    (t.tileCalculator.tileLonLat _).when(14, *, *).returns(new Tile(14, 1, 1))

    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(true)

    t.poiChangeAnalyzer.analyze(osmChange)

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
        "poi-tile-task:14-0-0",
        "poi-tile-task:13-1-1",
        "poi-tile-task:14-1-1"
      )
    )
  }

  test("known node poi delete") {

    val t = new TestSetup()

    val node = newRawNode(
      id = 123L,
      latitude = "1",
      longitude = "2"
    )

    val osmChange = OsmChange(Seq(Change.delete(node)))

    val existingPoi = newPoi(
      elementType = "node",
      elementId = 123L,
      latitude = "1",
      longitude = "2",
      tags = Tags.from("shop" -> "bicycle"),
      layers = Seq("bicycle"),
      tiles = Seq("13-0-0", "14-0-0")
    )

    (t.poiRepository.poi _).when(PoiRef("node", 123)).returns(Some(existingPoi))

    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(true)

    t.poiChangeAnalyzer.analyze(osmChange)

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

    val node = newRawNode(
      id = 123L,
      latitude = "1",
      longitude = "2"
    )

    val osmChange = OsmChange(Seq(Change.delete(node)))

    val existingPoi = newPoi(
      elementType = "node",
      elementId = 123L,
      latitude = "1",
      longitude = "2",
      tags = Tags.from("shop" -> "bicycle"),
      layers = Seq("bicycle"),
      tiles = Seq("13-0-0", "14-0-0")
    )

    (t.poiRepository.poi _).when(PoiRef("node", 123)).returns(Some(existingPoi))

    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(false)

    t.poiChangeAnalyzer.analyze(osmChange)

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

    val node = newRawNode(
      id = 123L,
      latitude = "1",
      longitude = "2"
    )

    val osmChange = OsmChange(Seq(Change.delete(node)))

    (t.poiRepository.poi _).when(PoiRef("node", 123)).returns(None)

    (t.knownPoiCache.contains _).when(PoiRef("node", 123)).returns(false)

    t.poiChangeAnalyzer.analyze(osmChange)

    (t.poiRepository.delete _).verify(*).never()

    (t.knownPoiCache.delete _).verify(
      where { poiRef: PoiRef =>
        poiRef.elementType should equal("node")
        poiRef.elementId should equal(123)
        true
      }
    ).once()

    t.taskRepository.all(PoiTileTask.prefix) should equal(Seq())
  }

  private class TestSetup {

    val poiRepository: PoiRepository = stub[PoiRepository]
    val knownPoiCache: KnownPoiCache = stub[KnownPoiCache]
    val tileCalculator: TileCalculator = stub[TileCalculator]
    val taskRepository: TaskRepository = new MockTaskRepository()
    val poiQueryExecutor: PoiQueryExecutor = stub[PoiQueryExecutor]
    val poiScopeAnalyzer: PoiScopeAnalyzer = stub[PoiScopeAnalyzer]

    val poiChangeAnalyzer: PoiChangeAnalyzer = new PoiChangeAnalyzerImpl(
      knownPoiCache,
      poiRepository,
      tileCalculator,
      taskRepository,
      poiScopeAnalyzer,
      poiQueryExecutor
    )

  }

}
