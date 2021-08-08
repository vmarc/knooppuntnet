package kpn.server.analyzer.engine.changes.network

import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeSet
import kpn.api.common.changes.details.ChangeType
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.changes.network.create.NetworkCreateProcessor
import kpn.server.analyzer.engine.changes.network.delete.NetworkDeleteProcessor
import kpn.server.analyzer.engine.changes.network.update.NetworkUpdateProcessor
import org.scalamock.scalatest.MockFactory

import java.util.concurrent.CompletableFuture.completedFuture

class NetworkChangeProcessorTest extends UnitTest with MockFactory with SharedTestObjects {

  test("network creates are processed by createProcessor") {

    pending

    val t = new TestSetup()

    (t.changeAnalyzer.analyze _).when(*).returns(ElementChanges(creates = Seq(t.createdNetworkId)))

    val changeSetChanges = t.processor.process(t.context)

    changeSetChanges should matchTo(t.createChangeSetChanges)

    (t.updateProcessor.process _).verify(*, *).never()
    (t.deleteProcessor.process _).verify(*, *).never()
  }

  test("network updates are processed by updateProcessor") {

    pending

    val t = new TestSetup()

    (t.changeAnalyzer.analyze _).when(*).returns(ElementChanges(updates = Seq(t.updatedNetworkId)))

    val changeSetChanges = t.processor.process(t.context)

    changeSetChanges should matchTo(t.updateChangeSetChanges)

    (t.createProcessor.process _).verify(*, *).never()
    (t.deleteProcessor.process _).verify(*, *).never()
  }

  test("network deletes are processed by deleteProcessor") {

    pending

    val t = new TestSetup()

    (t.changeAnalyzer.analyze _).when(*).returns(ElementChanges(deletes = Seq(t.deletedNetworkId)))

    val changeSetChanges = t.processor.process(t.context)

    changeSetChanges should matchTo(t.deleteChangeSetChanges)

    (t.createProcessor.process _).verify(*, *).never()
    (t.updateProcessor.process _).verify(*, *).never()
  }

  class TestSetup() {

    val createdNetworkId = 1L
    val updatedNetworkId = 2L
    val deletedNetworkId = 3L

    val changeSet: ChangeSet = newChangeSet()

    val context: ChangeSetContext = ChangeSetContext(
      replicationId = ReplicationId(1),
      changeSet
    )

    val changeAnalyzer: NetworkChangeAnalyzer = stub[NetworkChangeAnalyzer]
    val createProcessor: NetworkCreateProcessor = stub[NetworkCreateProcessor]
    val updateProcessor: NetworkUpdateProcessor = stub[NetworkUpdateProcessor]
    val deleteProcessor: NetworkDeleteProcessor = stub[NetworkDeleteProcessor]

    val createChangeSetChanges: ChangeSetChanges = ChangeSetChanges(
      networkChanges = Seq(newNetworkChange(newChangeKey(elementId = createdNetworkId), ChangeType.Create))
    )

    val updateChangeSetChanges: ChangeSetChanges = ChangeSetChanges(
      networkChanges = Seq(newNetworkChange(newChangeKey(elementId = updatedNetworkId), ChangeType.Update))
    )

    val deleteChangeSetChanges: ChangeSetChanges = ChangeSetChanges(
      networkChanges = Seq(newNetworkChange(newChangeKey(elementId = deletedNetworkId), ChangeType.Delete))
    )

    (createProcessor.process _).when(context, createdNetworkId).returns(completedFuture(createChangeSetChanges))
    (updateProcessor.process _).when(context, updatedNetworkId).returns(completedFuture(updateChangeSetChanges))
    (deleteProcessor.process _).when(context, deletedNetworkId).returns(completedFuture(deleteChangeSetChanges))

    val processor = new NetworkChangeProcessorImpl(
      changeAnalyzer,
      //  createProcessor,
      //  updateProcessor,
      //  deleteProcessor,
      null
    )
  }

}
