package kpn.server.analyzer.engine.changes.network

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.changes.network.create.NetworkCreateProcessor
import kpn.server.analyzer.engine.changes.network.delete.NetworkDeleteProcessor
import kpn.server.analyzer.engine.changes.network.update.NetworkUpdateProcessor
import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeSet
import kpn.api.common.changes.details.ChangeType
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class NetworkChangeProcessorTest extends FunSuite with Matchers with MockFactory with SharedTestObjects {

  test("network creates are processed by createProcessor") {

    val t = new TestSetup()

    (t.changeAnalyzer.analyze _).when(*).returns(ElementChanges(creates = Seq(t.createdNetworkId)))

    val changeSetChanges = t.processor.process(t.context)

    changeSetChanges should equal(t.createChangeSetChanges)

    (t.updateProcessor.process _).verify(*, *).never()
    (t.deleteProcessor.process _).verify(*, *).never()
  }

  test("network updates are processed by updateProcessor") {
    val t = new TestSetup()

    (t.changeAnalyzer.analyze _).when(*).returns(ElementChanges(updates = Seq(t.updatedNetworkId)))

    val changeSetChanges = t.processor.process(t.context)

    changeSetChanges should equal(t.updateChangeSetChanges)

    (t.createProcessor.process _).verify(*, *).never()
    (t.deleteProcessor.process _).verify(*, *).never()
  }

  test("network deletes are processed by deleteProcessor") {
    val t = new TestSetup()

    (t.changeAnalyzer.analyze _).when(*).returns(ElementChanges(deletes = Seq(t.deletedNetworkId)))

    val changeSetChanges = t.processor.process(t.context)

    changeSetChanges should equal(t.deleteChangeSetChanges)

    (t.createProcessor.process _).verify(*, *).never()
    (t.updateProcessor.process _).verify(*, *).never()
  }

  class TestSetup() {

    val createdNetworkId = 1L
    val updatedNetworkId = 2L
    val deletedNetworkId = 3L

    val changeSet: ChangeSet = newChangeSet()

    val context = ChangeSetContext(
      replicationId = ReplicationId(1),
      changeSet
    )

    val changeAnalyzer: NetworkChangeAnalyzer = stub[NetworkChangeAnalyzer]
    val createProcessor: NetworkCreateProcessor = stub[NetworkCreateProcessor]
    val updateProcessor: NetworkUpdateProcessor = stub[NetworkUpdateProcessor]
    val deleteProcessor: NetworkDeleteProcessor = stub[NetworkDeleteProcessor]

    val createChangeSetChanges = ChangeSetChanges(
      networkChanges = Seq(newNetworkChange(newChangeKey(elementId = createdNetworkId), ChangeType.Create))
    )

    val updateChangeSetChanges = ChangeSetChanges(
      networkChanges = Seq(newNetworkChange(newChangeKey(elementId = updatedNetworkId), ChangeType.Update))
    )

    val deleteChangeSetChanges = ChangeSetChanges(
      networkChanges = Seq(newNetworkChange(newChangeKey(elementId = deletedNetworkId), ChangeType.Delete))
    )

    (createProcessor.process _).when(context, createdNetworkId).returns(Future(createChangeSetChanges))
    (updateProcessor.process _).when(context, updatedNetworkId).returns(Future(updateChangeSetChanges))
    (deleteProcessor.process _).when(context, deletedNetworkId).returns(Future(deleteChangeSetChanges))

    val processor = new NetworkChangeProcessorImpl(
      changeAnalyzer,
      createProcessor,
      updateProcessor,
      deleteProcessor
    )
  }

}
