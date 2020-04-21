package kpn.server.analyzer.engine.changes.network.create

import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.load.NetworkLoader
import kpn.server.analyzer.load.data.LoadedNetwork
import kpn.core.test.TestData
import kpn.core.util.Log
import kpn.core.util.MockLog
import kpn.server.analyzer.engine.analysis.network.NetworkRelationAnalyzer
import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeSet
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

class NetworkCreateProcessorWorkerTest extends FunSuite with Matchers with MockFactory with SharedTestObjects {

  test("network create - network 'after' situation cannot be loaded") {

    val t = new TestSetup()
    (t.networkLoader.load _).when(Some(timestampAfterValue), t.networkId).returns(None)

    t.networkCreateProcessor.process(t.context, t.networkId)

    (t.watchedProcessor.process _).verify(*, *).never()

    t.mockLog.messages should equal(
      Seq(
        """|ERROR Processing network create from changeset 000/000/001
           |Could not load network with id 123 at 2015-08-11 00:00:04.
           |Continue processing changeset without this network.""".stripMargin,
        "DEBUG 0 change(s)"
      )
    )
  }

  test("a regular network is processed by NetworkCreateWatchedProcessor") {

    val t = new TestSetup()

    val loadedNetwork = {
      val d = new TestData() {
        networkRelation(t.networkId, "name", Seq())
      }
      LoadedNetwork(
        t.networkId,
        networkType = NetworkType.hiking,
        name = "",
        data = d.data,
        relation = d.data.relations(t.networkId)
      )
    }

    (t.networkLoader.load _).when(Some(timestampAfterValue), t.networkId).returns(Some(loadedNetwork))

    (t.watchedProcessor.process _).when(t.context, loadedNetwork).returns(ChangeSetChanges(networkChanges = Seq(newNetworkChange())))

    t.networkCreateProcessor.process(t.context, t.networkId)

    t.mockLog.messages should equal(Seq("DEBUG 1 change(s)"))
  }

  test("fatal error") {

    val t = new TestSetup()

    (t.networkLoader.load _).when(*, *).throwing(new IllegalStateException())

    try {
      t.networkCreateProcessor.process(t.context, t.networkId)
    }
    catch {
      case e: IllegalStateException =>
    }

    t.mockLog.messages should equal(
      Seq(
        "ERROR Exception while processing network create (networkId=123) at 2015-08-11 00:00:04 in changeset 000/000/001."
      )
    )
  }

  class TestSetup() {

    val networkId = 123L

    val changeSet: ChangeSet = newChangeSet()

    val context = ChangeSetContext(
      replicationId = ReplicationId(1),
      changeSet
    )

    val networkLoader: NetworkLoader = stub[NetworkLoader]
    val watchedProcessor: NetworkCreateWatchedProcessor = stub[NetworkCreateWatchedProcessor]
    val networkRelationAnalyzer: NetworkRelationAnalyzer = stub[NetworkRelationAnalyzer]

    val mockLog = new MockLog()

    val networkCreateProcessor: NetworkCreateProcessorWorkerImpl = new NetworkCreateProcessorWorkerImpl(
      networkLoader,
      networkRelationAnalyzer,
      watchedProcessor
    ) {
      override val log: Log = mockLog
    }
  }

}
