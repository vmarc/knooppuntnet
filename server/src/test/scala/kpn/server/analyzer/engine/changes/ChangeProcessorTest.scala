package kpn.server.analyzer.engine.changes

import kpn.api.common.ReplicationId
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdater
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.changes.integration.AbstractTest
import kpn.server.analyzer.engine.changes.network.NetworkChangeProcessor
import kpn.server.analyzer.engine.changes.node.NodeChangeProcessor
import kpn.server.analyzer.engine.changes.orphan.route.OrphanRouteChangeProcessor
import kpn.server.repository.ChangeSetRepository

class ChangeProcessorTest extends AbstractTest {

  test("changeset not saved when there are no relevant changes") {

    withDatabase { database =>
      val tc = new TestContext(database, OverpassData.empty, OverpassData.empty)

      val context: ChangeSetContext = {
        val replicationId = ReplicationId(1)
        val changeSet = newChangeSet()
        val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
        ChangeSetContext(replicationId, changeSet, elementIds)
      }

      tc.changeProcessor.process(context)

      assert(database.changeSetSummaries.findAll().isEmpty)
      assert(database.nodeChanges.findAll().isEmpty)
      assert(database.routeChanges.findAll().isEmpty)
      assert(database.networkChanges.findAll().isEmpty)
      assert(database.nodes.findAll().isEmpty)
      assert(database.routes.findAll().isEmpty)
      assert(database.networks.findAll().isEmpty)
    }
  }

  test("changeSet is saved and changeSetInfo is fetched when there is a least one change") {

    pending

    val t = new TestSetup()

    val changeSetId = 333L

    val context: ChangeSetContext = {
      val replicationId = ReplicationId(1)
      val changeSet = newChangeSet(changeSetId)
      val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
      ChangeSetContext(replicationId, changeSet, elementIds)
    }

    val networkChanges = ChangeSetChanges(
      networkChanges = Seq(newNetworkChange())
    )

    (t.networkChangeProcessor.process _).when(*).returns(networkChanges)
    (t.orphanRouteChangeProcessor.process _).when(*).returns(ChangeSetChanges())
    (t.orphanNodeChangeProcessor.process _).when(*, *).returns(ChangeSetChanges())

    t.changeProcessor.process(context)

    (t.changeSetInfoUpdater.changeSetInfo _).verify(changeSetId).once()
    (t.changeSaver.save _).verify(ReplicationId(1), context.changeSet, networkChanges).once()

  }

  test("network, orphan route and orphan node changes are merged") {

    pending

    val t = new TestSetup()

    val context: ChangeSetContext = {
      val replicationId = ReplicationId(1)
      val changeSet = newChangeSet()
      val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
      ChangeSetContext(replicationId, changeSet, elementIds)
    }

    val networkChange = newNetworkChange()
    val routeChange = newRouteChange()
    val nodeChange = newNodeChange()

    val networkChanges = ChangeSetChanges(
      networkChanges = Seq(networkChange)
    )

    val orphanRouteChanges = ChangeSetChanges(
      routeChanges = Seq(routeChange)
    )

    val orphanNodeChanges = ChangeSetChanges(
      nodeChanges = Seq(nodeChange)
    )

    val mergedChanges = ChangeSetChanges(
      Seq.empty,
      Seq(networkChange),
      Seq(routeChange),
      Seq(nodeChange)
    )

    (t.networkChangeProcessor.process _).when(*).returns(networkChanges)
    (t.orphanRouteChangeProcessor.process _).when(*).returns(orphanRouteChanges)
    (t.orphanNodeChangeProcessor.process _).when(*, *).returns(orphanNodeChanges)

    t.changeProcessor.process(context)

    (t.changeSaver.save _).verify(ReplicationId(1), context.changeSet, mergedChanges).once()
  }

  class TestSetup() {

    val changeSetRepository: ChangeSetRepository = stub[ChangeSetRepository]
    val networkChangeProcessor: NetworkChangeProcessor = stub[NetworkChangeProcessor]
    val orphanRouteChangeProcessor: OrphanRouteChangeProcessor = stub[OrphanRouteChangeProcessor]
    val orphanNodeChangeProcessor: NodeChangeProcessor = stub[NodeChangeProcessor]
    val changeSetInfoUpdater: ChangeSetInfoUpdater = stub[ChangeSetInfoUpdater]
    val changeSaver: ChangeSaver = stub[ChangeSaver]

    val changeProcessor = new ChangeProcessor(
      null,
      networkChangeProcessor,
      orphanRouteChangeProcessor,
      orphanNodeChangeProcessor,
      changeSetInfoUpdater,
      changeSaver
    )
  }

}
