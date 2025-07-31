package kpn.server.analyzer.engine.monitor.state

import kpn.api.base.ObjectId
import kpn.core.test.TestObjects.newMonitorState
import kpn.core.test.TestObjects.newMonitorStateTile
import kpn.core.util.UnitTest
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.domain.MonitorStateTile
import kpn.server.monitor.repository.MonitorRouteRepository
import org.scalamock.scalatest.MockFactory

class MonitorStateStoreTest extends UnitTest with MockFactory {

  test("save monitor route state and state tiles") {
    // Setup test data
    val testData = setupTestData()
    val dependencies = setupMocks(testData)

    // Execute
    val store = new MonitorStateStore(dependencies.repository, dependencies.tileBuilder)
    store.saveState(testData.monitorState)

    // Verify
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
    repository: MonitorRouteRepository,
    tileBuilder: MonitorStateTileBuilder
  )

  private def setupTestData(): TestData = {
    val routeId = ObjectId("routeId")
    val relationId = 1L

    val monitorState = newMonitorState(
      _id = ObjectId("state-id"),
      routeId = routeId,
      relationId = relationId
    )

    val unchangedTile = createMonitorStateTile(1)
    val originalTileToUpdate = createMonitorStateTile(2)
    val tileToRemove = createMonitorStateTile(3)
    val updatedTile = originalTileToUpdate.copy(_id = ObjectId("update-key"), matchesLines = Seq("match"))
    val newTile = createMonitorStateTile(4)

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
    (repository.stateTiles _).when(*, *).returns(testData.existingTiles)

    val tileBuilder = stub[MonitorStateTileBuilder]
    (tileBuilder.build _).when(testData.monitorState).returns(testData.updatedTiles)

    TestDependencies(repository, tileBuilder)
  }

  private def verifyRepositoryCalls(repository: MonitorRouteRepository, testData: TestData): Unit = {
    // Verify state is saved
    (repository.saveState _).verify(testData.monitorState)

    // Verify obsolete tile is deleted
    (repository.deleteStateTile _).verify(testData.tileToRemove._id)

    // Verify updated tile keeps its original ID
    (repository.saveStateTile _).verify(testData.updatedTile.copy(_id = testData.originalTileToUpdate._id))

    // Verify new tile is saved
    (repository.saveStateTile _).verify(testData.newTile)
  }

  private def createMonitorStateTile(id: Int): MonitorStateTile = {
    newMonitorStateTile(
      _id = ObjectId(id.toString),
      routeId = ObjectId("routeId"),
      relationId = 1L,
      z = id,
      x = id,
      y = id
    )
  }
}
