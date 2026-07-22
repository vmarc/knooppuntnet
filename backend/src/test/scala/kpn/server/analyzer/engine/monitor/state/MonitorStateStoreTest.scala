package kpn.server.analyzer.engine.monitor.state

import kpn.core.test.TestObjects.newMonitorState
import kpn.core.test.TestObjects.newMonitorStateTile
import kpn.core.util.UnitTest
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.domain.MonitorStateTile
import kpn.server.monitor.repository.MonitorRouteRepository
import org.bson.types.ObjectId
import org.scalamock.stubs.Stub
import org.scalamock.stubs.Stubs

class MonitorStateStoreTest extends UnitTest with Stubs {

  test("save monitor route state and state tiles") {
    // setup
    val testData = setupTestData()
    val dependencies = setupMocks(testData)

    // execute
    val store = new MonitorStateStore(dependencies.repository, dependencies.tileBuilder)
    store.saveState(testData.monitorState)

    // verify
    verifyRepositoryCalls(dependencies.repository, testData)
  }

  private case class TestData(
    routeId: ObjectId,
    relationId: Long,
    monitorState: MonitorState,
    unchangedTile: MonitorStateTile,
    originalTileToUpdate: MonitorStateTile,
    updatedTile: MonitorStateTile,
    tileToRemove: MonitorStateTile,
    newTile: MonitorStateTile,
    existingTiles: Seq[MonitorStateTile],
    updatedTiles: Seq[MonitorStateTile]
  )

  private case class TestDependencies(
    repository: Stub[MonitorRouteRepository],
    tileBuilder: Stub[MonitorStateTileBuilder]
  )

  private def setupTestData(): TestData = {
    val routeId = new ObjectId("123456789012345678901001")
    val relationId = 1L

    val monitorState = newMonitorState(
      _id = new ObjectId("123456789012345678901002"),
      routeId = routeId,
      relationId = relationId
    )

    val unchangedTile = createMonitorStateTile(new ObjectId("123456789012345678900001"), 1)
    val originalTileToUpdate = createMonitorStateTile(new ObjectId("123456789012345678900002"), 2)
    val tileToRemove = createMonitorStateTile(new ObjectId("123456789012345678900003"), 3)
    val updatedTile = originalTileToUpdate.copy(_id = new ObjectId("123456789012345678900003"), matchesLines = Seq("match"))
    val newTile = createMonitorStateTile(new ObjectId("123456789012345678900004"), 4)

    val existingTiles = Seq(unchangedTile, originalTileToUpdate, tileToRemove)
    val updatedTiles = Seq(unchangedTile, updatedTile, newTile)

    TestData(
      routeId,
      relationId,
      monitorState,
      unchangedTile,
      originalTileToUpdate,
      updatedTile,
      tileToRemove,
      newTile,
      existingTiles,
      updatedTiles
    )
  }

  private def setupMocks(testData: TestData): TestDependencies = {
    val repository = stub[MonitorRouteRepository]
    (repository.stateTiles _).returnsWith(testData.existingTiles)
    (repository.saveState _).returnsWith(())
    (repository.deleteStateTile _).returnsWith(())
    (repository.saveStateTile _).returnsWith(())

    val tileBuilder = stub[MonitorStateTileBuilder]
    (tileBuilder.build _).returnsWith(testData.updatedTiles)

    TestDependencies(repository, tileBuilder)
  }

  private def verifyRepositoryCalls(repository: Stub[MonitorRouteRepository], testData: TestData): Unit = {
    // verify state is saved
    (repository.saveState _).calls should equal(Seq(testData.monitorState))

    // verify obsolete tile is deleted
    (repository.deleteStateTile _).calls should equal(Seq(testData.tileToRemove._id))

    // verify new tile is saved
    // verify updated tile keeps its original ID
    assertEqual(
      (repository.saveStateTile _).calls,
      Seq(
        testData.newTile,
        testData.updatedTile.copy(_id = testData.originalTileToUpdate._id)
      )
    )
  }

  private def createMonitorStateTile(_id: ObjectId, id: Int): MonitorStateTile = {
    newMonitorStateTile(
      _id = _id,
      routeId = new ObjectId("123456789012345678901001"),
      relationId = 1L,
      z = id,
      x = id,
      y = id
    )
  }
}
