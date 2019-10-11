package kpn.server.repository

import kpn.core.test.TestSupport.withDatabase
import kpn.shared.ChangeSetNetwork
import kpn.shared.ChangeSetSummary
import kpn.shared.Country
import kpn.shared.NetworkChanges
import kpn.shared.NetworkType
import kpn.shared.ReplicationId
import kpn.shared.Subset
import kpn.shared.Timestamp
import kpn.shared.changes.ChangeSetData
import kpn.shared.changes.details.ChangeKey
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.RefChanges
import kpn.shared.changes.details.RouteChange
import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.diff.IdDiffs
import kpn.shared.diff.RefDiffs
import kpn.shared.diff.common.FactDiffs
import kpn.shared.diff.route.RouteDiff
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ChangeSetRepositoryTest extends FunSuite with Matchers {

  test("change set not found") {
    withChangeSetRepository { repository =>
      repository.changeSet(0L, None, stale = false) should equal(Seq())
      repository.changeSet(0L, Some(ReplicationId(1, 2, 3)), stale = false) should equal(Seq())
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
      repository.saveNetworkChange(networkChange1)
      repository.saveRouteChange(routeChange1)
      repository.saveNodeChange(nodeChange1)

      repository.saveChangeSetSummary(changeSetSummary2)
      repository.saveNetworkChange(networkChange2)
      repository.saveRouteChange(routeChange2)
      repository.saveNodeChange(nodeChange2)

      repository.changeSet(changeSetId, Some(ReplicationId(replicationNumber1)), stale = false) should equal(
        Seq(
          ChangeSetData(
            changeSetSummary1,
            Seq(networkChange1),
            Seq(routeChange1),
            Seq(nodeChange1)
          )
        )
      )

      repository.changeSet(changeSetId, Some(ReplicationId(replicationNumber2)), stale = false) should equal(
        Seq(
          ChangeSetData(
            changeSetSummary2,
            Seq(networkChange2),
            Seq(routeChange2),
            Seq(nodeChange2)
          )
        )
      )

      repository.changeSet(changeSetId, None, stale = false) should equal(
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

  test("changes") {

    withChangeSetRepository { repository =>

      repository.saveChangeSetSummary(summary(1, 11, Timestamp(2015, 8, 11), subsets = Seq(Subset.nlHiking)))
      repository.saveChangeSetSummary(summary(2, 12, Timestamp(2015, 8, 12), subsets = Seq(Subset.nlHiking)))
      repository.saveChangeSetSummary(summary(3, 13, Timestamp(2015, 8, 13), subsets = Seq(Subset.nlHiking), happy = false))
      repository.saveChangeSetSummary(summary(4, 14, Timestamp(2015, 9, 11), subsets = Seq(Subset.nlHiking), happy = false))
      repository.saveChangeSetSummary(summary(5, 15, Timestamp(2015, 9, 12), subsets = Seq(Subset.beHiking), happy = false))

      def changes(parameters: ChangesParameters): Seq[Long] = {
        repository.changes(parameters, stale = false).map(_.key.changeSetId)
      }

      changes(ChangesParameters(impact = false)) should equal(Seq(5, 4, 3, 2, 1))
      changes(ChangesParameters(impact = true)) should equal(Seq(2, 1))

      //      changes(subset = Some(Subset.nlHiking)) should equal(Seq(4, 3, 2, 1))

      //      changes(month = Some(ChangesMonthParameter("2015", "08"))) should equal(Seq(3, 2, 1))
      //
      //      // FROM NO LONGER RELEVANT changes(from = Some(from(2015, 8, 13, 3, 13, 0))) should equal(Seq(5, 4, 3))
      //
      //      // FROM NO LONGER RELEVANT changes(from = Some(from(2015, 9, 11, 4, 14, 0))) should equal(Seq(5, 4))
      //
      //      changes(impact = true) should equal(Seq(2, 1))
    }
  }

  test("changes filters on subset") {

    withChangeSetRepository { repository =>

      repository.saveChangeSetSummary(summary(1, 11, subsets = Seq(Subset.nlHiking)))
      repository.saveChangeSetSummary(summary(2, 12, subsets = Seq(Subset.nlHiking)))
      repository.saveChangeSetSummary(summary(3, 13, subsets = Seq(Subset.beHiking)))

      def changes(parameters: ChangesParameters): Seq[Long] = {
        repository.changes(parameters, stale = false).map(_.key.changeSetId)
      }

      changes(ChangesParameters(Some(Subset.nlHiking))) should equal(Seq(2, 1))
      changes(ChangesParameters(Some(Subset.beHiking))) should equal(Seq(3))
    }
  }

  //  test("changesPerMonth") {
  //
  //    withChangeSetRepository { repository =>
  //
  //      repository.saveChangeSetSummary(summary(1, ReplicationId(11), Timestamp(2014, 8, 1), subset = Subset.nlHiking, happy = true))
  //      repository.saveChangeSetSummary(summary(2, ReplicationId(12), Timestamp(2014, 8, 1), subset = Subset.nlHiking, happy = false))
  //      repository.saveChangeSetSummary(summary(3, ReplicationId(13), Timestamp(2014, 9, 1), subset = Subset.nlHiking, happy = true))
  //      repository.saveChangeSetSummary(summary(4, ReplicationId(14), Timestamp(2014, 9, 1), subset = Subset.nlHiking, happy = false))
  //      repository.saveChangeSetSummary(summary(5, ReplicationId(15), Timestamp(2014, 9, 1), subset = Subset.nlHiking, happy = false))
  //      repository.saveChangeSetSummary(summary(6, ReplicationId(16), Timestamp(2015, 1, 1), subset = Subset.nlHiking, happy = true))
  //      repository.saveChangeSetSummary(summary(7, ReplicationId(17), Timestamp(2015, 1, 1), subset = Subset.beHiking, happy = true))
  //
  //      repository.changesPerMonth(None, stale = false) should equal(
  //        ???
  ////        ChangesFilter(
  ////          Seq(
  ////            ChangesFilterYear(
  ////              "2015",
  ////              Seq(
  ////                ChangesFilterMonth("2015", "01", 2, 2)
  ////              )
  ////            ),
  ////            ChangesFilterYear(
  ////              "2014",
  ////              Seq(
  ////                ChangesFilterMonth("2014", "09", 3, 1),
  ////                ChangesFilterMonth("2014", "08", 2, 1)
  ////              )
  ////            )
  ////          )
  ////        )
  //      )
  //
  //      repository.changesPerMonth(Some(Subset.nlHiking), stale = false) should equal(
  //        ???
  ////        ChangesFilter(
  ////          Seq(
  ////            ChangesFilterYear(
  ////              "2015",
  ////              Seq(
  ////                ChangesFilterMonth("2015", "01", 1, 1)
  ////              )
  ////            ),
  ////            ChangesFilterYear(
  ////              "2014",
  ////              Seq(
  ////                ChangesFilterMonth("2014", "09", 3, 1),
  ////                ChangesFilterMonth("2014", "08", 2, 1)
  ////              )
  ////            )
  ////          )
  ////        )
  //      )
  //    }
  //  }

  test("networkChanges") {
    withChangeSetRepository { repository =>

      repository.saveNetworkChange(networkChange(11L, 101, 1L, timestamp = Timestamp(2015, 1, 1)))
      repository.saveNetworkChange(networkChange(12L, 102, 1L, timestamp = Timestamp(2015, 2, 1)))
      repository.saveNetworkChange(networkChange(13L, 103, 1L, timestamp = Timestamp(2015, 3, 1)))
      repository.saveNetworkChange(networkChange(14L, 104, 1L, timestamp = Timestamp(2015, 4, 1)))

      repository.networkChanges(ChangesParameters(networkId = Some(1L)), stale = false).map(_.key.changeSetId) should equal(Seq(14, 13, 12, 11))
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

    ChangeSetSummary(
      ChangeKey(
        replicationNumber,
        timestamp,
        changeSetId,
        0L
      ),
      subsets = subsets,
      timestampFrom = timestamp,
      timestampUntil = timestamp,
      NetworkChanges(creates = networkChangesCreates),
      Seq(),
      Seq(),
      Seq(),
      happy
    )
  }

  private def networkChange(
    changeSetId: Long,
    replicationNumber: Int,
    networkId: Long,
    timestamp: Timestamp = Timestamp(2015, 8, 11),
    happy: Boolean = true): NetworkChange = {

    NetworkChange(
      ChangeKey(
        replicationNumber,
        timestamp,
        changeSetId,
        networkId
      ),
      ChangeType.Update,
      Some(Country.nl),
      NetworkType.hiking,
      networkId,
      "network" + networkId,
      RefChanges(),
      RefChanges(),
      None,
      RefDiffs.empty,
      RefDiffs.empty,
      IdDiffs.empty,
      IdDiffs.empty,
      IdDiffs.empty,
      happy = true,
      investigate = true
    )
  }

  private def routeChange(
    changeSetId: Long,
    replicationNumber: Int,
    routeId: Long,
    timestamp: Timestamp = Timestamp(2015, 8, 11),
    name: String = "name"
  ): RouteChange = {

    RouteChange(
      ChangeKey(
        replicationNumber,
        timestamp,
        changeSetId,
        routeId
      ),
      ChangeType.Update,
      name = name,
      addedToNetwork = Seq.empty,
      removedFromNetwork = Seq.empty,
      before = None,
      after = None,
      removedWays = Seq.empty,
      addedWays = Seq.empty,
      updatedWays = Seq.empty,
      diffs = RouteDiff(),
      facts = Seq.empty
    )
  }

  private def nodeChange(
    changeSetId: Long,
    replicationNumber: Int,
    nodeId: Long,
    timestamp: Timestamp = Timestamp(2015, 8, 11),
    name: String = "name"
  ): NodeChange = {

    NodeChange(
      ChangeKey(
        replicationNumber,
        timestamp,
        changeSetId,
        nodeId
      ),
      ChangeType.Update,
      subsets = Seq.empty,
      name = name,
      before = None,
      after = None,
      connectionChanges = Seq.empty,
      roleConnectionChanges = Seq.empty,
      definedInNetworkChanges = Seq.empty,
      tagDiffs = None,
      nodeMoved = None,
      addedToRoute = Seq.empty,
      removedFromRoute = Seq.empty,
      addedToNetwork = Seq.empty,
      removedFromNetwork = Seq.empty,
      factDiffs = FactDiffs(),
      facts = Seq.empty
    )
  }

  private def withChangeSetRepository(f: ChangeSetRepository => Unit): Unit = {
    withDatabase() { database =>
      val repository: ChangeSetRepository = new ChangeSetRepositoryImpl(database)
      f(repository)
    }
  }
}
