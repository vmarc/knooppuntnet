package kpn.core.engine.changes.network.create

import kpn.core.engine.analysis.NetworkRelationAnalyzer
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges
import kpn.core.engine.changes.ignore.IgnoredNetworkAnalyzer
import kpn.core.load.NetworkLoader
import kpn.core.load.data.LoadedNetwork
import kpn.core.test.TestData
import kpn.core.util.MockLog
import kpn.shared.Fact
import kpn.shared.NetworkType
import kpn.shared.ReplicationId
import kpn.shared.SharedTestObjects
import kpn.shared.changes.ChangeSet
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NetworkCreateProcessorWorkerTest extends FunSuite with Matchers with MockFactory with SharedTestObjects {

  test("network create - network 'after' situation cannot be loaded") {

    val t = new TestSetup()
    (t.networkLoader.load _).when(Some(timestampAfterValue), t.networkId).returns(None)

    t.networkCreateProcessor.process(t.context, t.networkId)

    (t.ignoredNetworkAnalyzer.analyze _).verify(*, *).never()
    (t.watchedProcessor.process _).verify(*, *).never()
    (t.ignoredProcessor.process _).verify(*, *, *).never()

    t.log.messages should equal(
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

    (t.ignoredNetworkAnalyzer.analyze _).when(*, *).returns(Seq())

    (t.networkLoader.load _).when(Some(timestampAfterValue), t.networkId).returns(Some(loadedNetwork))

    (t.watchedProcessor.process _).when(t.context, loadedNetwork).returns(ChangeSetChanges(networkChanges = Seq(newNetworkChange())))

    t.networkCreateProcessor.process(t.context, t.networkId)

    (t.ignoredProcessor.process _).verify(*, *, *).never()

    t.log.messages should equal(Seq("DEBUG 1 change(s)"))
  }

  test("an ignored network is processed by NetworkCreateIgnoredProcessor") {

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

    (t.ignoredNetworkAnalyzer.analyze _).when(*, *).returns(Seq(Fact.IgnoreForeignCountry))
    (t.networkLoader.load _).when(*, *).returns(Some(loadedNetwork))

    t.networkCreateProcessor.process(t.context, t.networkId)

    (t.ignoredProcessor.process _).verify(t.context, loadedNetwork, Seq(Fact.IgnoreForeignCountry)).once()
    (t.watchedProcessor.process _).verify(*, *).never()

    t.log.messages should equal(Seq("DEBUG 0 change(s)"))
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

    t.log.messages should equal(
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
    val ignoredNetworkAnalyzer: IgnoredNetworkAnalyzer = stub[IgnoredNetworkAnalyzer]
    val watchedProcessor: NetworkCreateWatchedProcessor = stub[NetworkCreateWatchedProcessor]
    val ignoredProcessor: NetworkCreateIgnoredProcessor = stub[NetworkCreateIgnoredProcessor]
    val networkRelationAnalyzer: NetworkRelationAnalyzer = stub[NetworkRelationAnalyzer]

    val log = new MockLog()

    val networkCreateProcessor = new NetworkCreateProcessorWorkerImpl(
      networkLoader,
      networkRelationAnalyzer,
      ignoredNetworkAnalyzer,
      watchedProcessor,
      ignoredProcessor,
      log
    )
  }

}
