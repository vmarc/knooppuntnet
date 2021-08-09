package kpn.server.analyzer.engine.changes

import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdater
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.changes.network.NetworkChangeProcessor
import kpn.server.analyzer.engine.changes.orphan.node.OrphanNodeChangeProcessor
import kpn.server.analyzer.engine.changes.orphan.route.OrphanRouteChangeProcessor
import kpn.server.repository.ChangeSetRepository
import org.scalamock.scalatest.MockFactory

class ChangeProcessorTest extends UnitTest with MockFactory with SharedTestObjects {

  test("changeset not saved when there are no relevant changes") {

    val t = new TestSetup()

    val context: ChangeSetContext = {
      val replicationId = ReplicationId(1)
      val changeSet = newChangeSet()
      val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
      ChangeSetContext(replicationId, changeSet, elementIds)
    }

    (t.networkChangeProcessor.process _).when(*).returns(ChangeSetChanges())
    (t.orphanRouteChangeProcessor.process _).when(*).returns(ChangeSetChanges())
    (t.orphanNodeChangeProcessor.process _).when(*).returns(ChangeSetChanges())

    t.changeProcessor.process(context)

    (t.networkChangeProcessor.process _).verify(context).once()
    (t.orphanRouteChangeProcessor.process _).verify(context).once()
    (t.orphanNodeChangeProcessor.process _).verify(context).once()

    (t.changeSetInfoUpdater.changeSetInfo _).verify(*).never()
    (t.changeSaver.save _).verify(*, *, *).never()
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
    (t.orphanNodeChangeProcessor.process _).when(*).returns(ChangeSetChanges())

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
    (t.orphanNodeChangeProcessor.process _).when(*).returns(orphanNodeChanges)

    t.changeProcessor.process(context)

    (t.changeSaver.save _).verify(ReplicationId(1), context.changeSet, mergedChanges).once()
  }

  class TestSetup() {

    val changeSetRepository: ChangeSetRepository = stub[ChangeSetRepository]
    val networkChangeProcessor: NetworkChangeProcessor = stub[NetworkChangeProcessor]
    val orphanRouteChangeProcessor: OrphanRouteChangeProcessor = stub[OrphanRouteChangeProcessor]
    val orphanNodeChangeProcessor: OrphanNodeChangeProcessor = stub[OrphanNodeChangeProcessor]
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
