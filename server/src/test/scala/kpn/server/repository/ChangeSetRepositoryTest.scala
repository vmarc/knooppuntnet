package kpn.server.repository

import kpn.api.common.ChangeSetNetwork
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSummary
import kpn.api.common.NetworkChanges
import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeSetData
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.ChangeType
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class ChangeSetRepositoryTest extends UnitTest with SharedTestObjects {

  test("change set not found") {
    withChangeSetRepository { repository =>
      repository.changeSet(0L, None) shouldBe empty
      repository.changeSet(0L, Some(ReplicationId(1, 2, 3))) shouldBe empty
    }
  }

  test("changeSet") {

    withChangeSetRepository { repository =>

      val changeSetId = 1L
      val replicationNumber1 = 11
      val replicationNumber2 = 12

      val changeSetSummary1 = summary(changeSetId, replicationNumber1)
      val networkChange1 = networkChange(changeSetId, replicationNumber1, 101L)
      val routeChange1 = routeChange(changeSetId, replicationNumber1, 1001L)
      val nodeChange1 = nodeChange(changeSetId, replicationNumber1, 10001L)

      val changeSetSummary2 = summary(changeSetId, replicationNumber2)
      val networkChange2 = networkChange(changeSetId, replicationNumber2, 102L)
      val routeChange2 = routeChange(changeSetId, replicationNumber2, 1002L)
      val nodeChange2 = nodeChange(changeSetId, replicationNumber2, 10002L)

      repository.saveChangeSetSummary(changeSetSummary1)
      repository.saveNetworkInfoChange(networkChange1)
      repository.saveRouteChange(routeChange1)
      repository.saveNodeChange(nodeChange1)

      repository.saveChangeSetSummary(changeSetSummary2)
      repository.saveNetworkInfoChange(networkChange2)
      repository.saveRouteChange(routeChange2)
      repository.saveNodeChange(nodeChange2)

      repository.changeSet(changeSetId, Some(ReplicationId(replicationNumber1))) should equal(
        Seq(
          ChangeSetData(
            changeSetSummary1,
            Seq(networkChange1),
            Seq(routeChange1),
            Seq(nodeChange1)
          )
        )
      )

      repository.changeSet(changeSetId, Some(ReplicationId(replicationNumber2))) should equal(
        Seq(
          ChangeSetData(
            changeSetSummary2,
            Seq(networkChange2),
            Seq(routeChange2),
            Seq(nodeChange2)
          )
        )
      )

      repository.changeSet(changeSetId, None) should equal (
        Seq(
          ChangeSetData(
            changeSetSummary1,
            Seq(networkChange1),
            Seq(routeChange1),
            Seq(nodeChange1)
          ),
          ChangeSetData(
            changeSetSummary2,
            Seq(networkChange2),
            Seq(routeChange2),
            Seq(nodeChange2)
          )
        )
      )
    }
  }

  test("changes filters on subset") {

    withChangeSetRepository { repository =>

      repository.saveChangeSetSummary(summary(1, 11, Timestamp(2015, 1, 1), subsets = Seq(Subset.nlHiking)))
      repository.saveChangeSetSummary(summary(2, 12, Timestamp(2015, 1, 2), subsets = Seq(Subset.nlHiking)))
      repository.saveChangeSetSummary(summary(3, 13, Timestamp(2015, 1, 3), subsets = Seq(Subset.beHiking)))

      def changes(subset: Subset, parameters: ChangesParameters): Seq[Long] = {
        repository.subsetChanges(subset, parameters).map(_.key.changeSetId)
      }

      changes(Subset.nlHiking, ChangesParameters()) should equal(Seq(2, 1))
      changes(Subset.beHiking, ChangesParameters()) should equal(Seq(3))
    }
  }

  test("changes") {

    withChangeSetRepository { repository =>

      repository.saveChangeSetSummary(summary(1, 1, timestamp = Timestamp(2015, 1, 1), subsets = Seq(Subset.nlHiking)))
      repository.saveChangeSetSummary(summary(2, 1, timestamp = Timestamp(2015, 1, 2), subsets = Seq(Subset.nlHiking)))
      repository.saveChangeSetSummary(summary(3, 1, timestamp = Timestamp(2015, 1, 3), subsets = Seq(Subset.nlHiking), happy = false))
      repository.saveChangeSetSummary(summary(4, 1, timestamp = Timestamp(2015, 1, 4), subsets = Seq(Subset.nlHiking), happy = false))
      repository.saveChangeSetSummary(summary(5, 1, timestamp = Timestamp(2015, 1, 5), subsets = Seq(Subset.beHiking), happy = false))

      def changes(parameters: ChangesParameters): Seq[Long] = {
        repository.changes(parameters).map(_.key.changeSetId)
      }

      changes(ChangesParameters()) should equal(Seq(5, 4, 3, 2, 1))
      changes(ChangesParameters(impact = true)) should equal(Seq(2, 1))
    }
  }

  test("networkChanges") {

    withChangeSetRepository { repository =>

      repository.saveNetworkInfoChange(networkChange(11, 1, 1, Timestamp(2015, 1, 1)))
      repository.saveNetworkInfoChange(networkChange(12, 1, 1, Timestamp(2015, 1, 2)))
      repository.saveNetworkInfoChange(networkChange(13, 1, 1, Timestamp(2015, 1, 3)))

      repository.networkChanges(1L, ChangesParameters()).map(_.key.changeSetId) should equal(Seq(13, 12, 11))
    }
  }

  test("routeChanges") {

    withChangeSetRepository { repository =>

      repository.saveRouteChange(routeChange(11, 1, 10, Timestamp(2015, 1, 1)))
      repository.saveRouteChange(routeChange(12, 1, 10, Timestamp(2015, 1, 2)))
      repository.saveRouteChange(routeChange(13, 1, 10, Timestamp(2015, 1, 3)))
      repository.saveRouteChange(routeChange(14, 1, 20, Timestamp(2015, 1, 4)))

      repository.routeChanges(10L, ChangesParameters()).map(_.key.changeSetId) should equal(Seq(13, 12, 11))
    }
  }

  test("nodeChanges") {

    withChangeSetRepository { repository =>

      repository.saveNodeChange(nodeChange(11, 1, 1001, Timestamp(2015, 1, 1)))
      repository.saveNodeChange(nodeChange(12, 1, 1001, Timestamp(2015, 1, 2)))
      repository.saveNodeChange(nodeChange(13, 1, 1001, Timestamp(2015, 1, 3)))
      repository.saveNodeChange(nodeChange(14, 1, 1002, Timestamp(2015, 1, 4)))

      repository.nodeChanges(1001L, ChangesParameters()).map(_.key.changeSetId) should equal(Seq(13, 12, 11))
    }
  }

  test("changesFilter") {

    withChangeSetRepository { repository =>

      repository.saveChangeSetSummary(summary(1, 1, subsets = Seq(Subset.nlHiking), timestamp = Timestamp(2015, 1, 1)))
      repository.saveChangeSetSummary(summary(2, 2, subsets = Seq(Subset.nlHiking), timestamp = Timestamp(2015, 2, 1)))
      repository.saveChangeSetSummary(summary(3, 3, subsets = Seq(Subset.beHiking), timestamp = Timestamp(2015, 3, 1)))

      repository.saveChangeSetSummary(summary(4, 4, subsets = Seq(Subset.beHiking), timestamp = Timestamp(2016, 1, 1)))
      repository.saveChangeSetSummary(summary(5, 5, subsets = Seq(Subset.beHiking), timestamp = Timestamp(2016, 2, 1)))

      repository.changesFilter(None, None, None, None) should equal(
        Seq(
          ChangesFilterOption("all", "all", None, None, None, 5, 5, current = true),
          ChangesFilterOption("year", "2016", Some(2016), None, None, 2, 2),
          ChangesFilterOption("year", "2015", Some(2015), None, None, 3, 3)
        )
      )

      repository.changesFilter(Some(Subset.nlHiking), None, None, None) should equal(
        Seq(
          ChangesFilterOption("all", "all", None, None, None, 2, 2, current = true),
          ChangesFilterOption("year", "2015", Some(2015), None, None, 2, 2)
        )
      )

      repository.changesFilter(Some(Subset.beHiking), Some(2016), None, None) should equal(
        Seq(
          ChangesFilterOption("all", "all", None, None, None, 3, 3),
          ChangesFilterOption("year", "2016", Some(2016), None, None, 2, 2, current = true),
          ChangesFilterOption("month", "2", Some(2016), Some(2), None, 1, 1),
          ChangesFilterOption("month", "1", Some(2016), Some(1), None, 1, 1),
          ChangesFilterOption("year", "2015", Some(2015), None, None, 1, 1)
        )
      )

      repository.changesFilter(Some(Subset.beHiking), Some(2016), Some(1), None) should equal(
        Seq(
          ChangesFilterOption("all", "all", None, None, None, 3, 3),
          ChangesFilterOption("year", "2016", Some(2016), None, None, 2, 2),
          ChangesFilterOption("month", "2", Some(2016), Some(2), None, 1, 1),
          ChangesFilterOption("month", "1", Some(2016), Some(1), None, 1, 1, current = true),
          ChangesFilterOption("day", "1", Some(2016), Some(1), Some(1), 1, 1),
          ChangesFilterOption("year", "2015", Some(2015), None, None, 1, 1)
        )
      )

      repository.changesFilter(Some(Subset.beHiking), Some(2016), Some(1), Some(1)) should equal(
        Seq(
          ChangesFilterOption("all", "all", None, None, None, 3, 3),
          ChangesFilterOption("year", "2016", Some(2016), None, None, 2, 2),
          ChangesFilterOption("month", "2", Some(2016), Some(2), None, 1, 1),
          ChangesFilterOption("month", "1", Some(2016), Some(1), None, 1, 1),
          ChangesFilterOption("day", "1", Some(2016), Some(1), Some(1), 1, 1, current = true),
          ChangesFilterOption("year", "2015", Some(2015), None, None, 1, 1)
        )
      )
    }
  }


  test("nodeChangesCount") {

    withChangeSetRepository { repository =>

      repository.saveNodeChange(nodeChange(1, 1, 1001L))
      repository.saveNodeChange(nodeChange(2, 2, 1001L))

      repository.nodeChangesCount(1001L) should equal(2)
      repository.nodeChangesCount(1002L) should equal(0)
    }
  }

  test("routeChangesCount") {

    withChangeSetRepository { repository =>

      repository.saveRouteChange(routeChange(1, 1, 10L))
      repository.saveRouteChange(routeChange(2, 2, 10L))

      repository.routeChangesCount(10L) should equal(2)
      repository.routeChangesCount(20L) should equal(0)
    }
  }

  test("networkChangesCount") {

    withChangeSetRepository { repository =>

      repository.saveNetworkInfoChange(networkChange(1, 1, 1L))
      repository.saveNetworkInfoChange(networkChange(2, 2, 1L))

      repository.networkChangesCount(1L) should equal(2)
      repository.networkChangesCount(2L) should equal(0)
    }
  }

  private def summary(
    changeSetId: Long,
    replicationNumber: Int,
    timestamp: Timestamp = Timestamp(2015, 1, 1),
    happy: Boolean = true,
    user: String = "user",
    subsets: Seq[Subset] = Seq(Subset.nlHiking),
    networkChangesCreates: Seq[ChangeSetNetwork] = Seq.empty
  ): ChangeSetSummary = {

    newChangeSetSummary(
      ChangeKey(
        replicationNumber,
        timestamp,
        changeSetId,
        0L
      ),
      subsets = subsets,
      timestampFrom = timestamp,
      timestampUntil = timestamp,
      networkChanges = NetworkChanges(creates = networkChangesCreates),
      subsetAnalyses = subsets.map(subset => ChangeSetSubsetAnalysis(subset, happy)),
      happy = happy
    )
  }

  private def networkChange(
    changeSetId: Long,
    replicationNumber: Int,
    networkId: Long,
    timestamp: Timestamp = Timestamp(2015, 8, 11),
    happy: Boolean = true
  ): NetworkInfoChange = {

    val key = ChangeKey(
      replicationNumber,
      timestamp,
      changeSetId,
      networkId
    )

    NetworkInfoChange(
      key.toId,
      key,
      ChangeType.Update,
      Some(Country.nl),
      NetworkType.hiking,
      networkId,
      "network" + networkId,
      None,
      RefDiffs.empty,
      RefDiffs.empty,
      IdDiffs.empty,
      IdDiffs.empty,
      IdDiffs.empty,
      happy = true,
      investigate = true,
      impact = true
    )
  }

  private def routeChange(
    changeSetId: Long,
    replicationNumber: Int,
    routeId: Long,
    timestamp: Timestamp = Timestamp(2015, 8, 11),
    name: String = "name"
  ): RouteChange = {

    newRouteChange(
      ChangeKey(
        replicationNumber,
        timestamp,
        changeSetId,
        routeId
      ),
      ChangeType.Update,
      name = name
    )
  }

  private def nodeChange(
    changeSetId: Long,
    replicationNumber: Int,
    nodeId: Long,
    timestamp: Timestamp = Timestamp(2015, 8, 11),
    name: String = "name"
  ): NodeChange = {

    newNodeChange(
      ChangeKey(
        replicationNumber,
        timestamp,
        changeSetId,
        nodeId
      ),
      ChangeType.Update,
      name = name
    )
  }

  private def withChangeSetRepository(f: ChangeSetRepository => Unit): Unit = {
    withDatabase { database =>
      val repository: ChangeSetRepository = new ChangeSetRepositoryImpl(database)
      f(repository)
    }
  }
}
