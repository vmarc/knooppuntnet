package kpn.server.repository

import kpn.api.common.ChangeSetNetwork
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSummary
import kpn.api.common.NetworkChanges
import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeSetData
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesFilterPeriod
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withCouchDatabase
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

      repository.changeSet(changeSetId, None) should matchTo(
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


  ignore("changes filters on subset") {

    withChangeSetRepository { repository =>

      repository.saveChangeSetSummary(summary(1, 11, subsets = Seq(Subset.nlHiking)))
      repository.saveChangeSetSummary(summary(2, 12, subsets = Seq(Subset.nlHiking)))
      repository.saveChangeSetSummary(summary(3, 13, subsets = Seq(Subset.beHiking)))

      def changes(subset: Subset, parameters: ChangesParameters): Seq[Long] = {
        repository.subsetChanges(subset, parameters).map(_.key.changeSetId)
      }

      changes(Subset.nlHiking, ChangesParameters()) should equal(Seq(2, 1))
      changes(Subset.beHiking, ChangesParameters()) should equal(Seq(3))
    }
  }

  test("changes") {

    withChangeSetRepository { repository =>

      repository.saveChangeSetSummary(summary(1, 1, subsets = Seq(Subset.nlHiking)))
      repository.saveChangeSetSummary(summary(2, 1, subsets = Seq(Subset.nlHiking)))
      repository.saveChangeSetSummary(summary(3, 1, subsets = Seq(Subset.nlHiking), happy = false))
      repository.saveChangeSetSummary(summary(4, 1, subsets = Seq(Subset.nlHiking), happy = false))
      repository.saveChangeSetSummary(summary(5, 1, subsets = Seq(Subset.beHiking), happy = false))

      def changes(parameters: ChangesParameters): Seq[Long] = {
        repository.changes(parameters).map(_.key.changeSetId)
      }

      changes(ChangesParameters(impact = false)) should equal(Seq(5, 4, 3, 2, 1))
      changes(ChangesParameters(impact = true)) should equal(Seq(2, 1))
    }
  }

  test("networkChanges") {

    withChangeSetRepository { repository =>

      repository.saveNetworkInfoChange(networkChange(11, 1, 1))
      repository.saveNetworkInfoChange(networkChange(12, 1, 1))
      repository.saveNetworkInfoChange(networkChange(13, 1, 1))

      repository.networkChanges(1L, ChangesParameters()).map(_.key.changeSetId) should equal(Seq(13, 12, 11))
    }
  }

  test("routeChanges") {

    withChangeSetRepository { repository =>

      repository.saveRouteChange(routeChange(11, 1, 10))
      repository.saveRouteChange(routeChange(12, 1, 10))
      repository.saveRouteChange(routeChange(13, 1, 10))
      repository.saveRouteChange(routeChange(14, 1, 20))

      repository.routeChanges(10L, ChangesParameters()).map(_.key.changeSetId) should equal(Seq(13, 12, 11))
    }
  }

  test("nodeChanges") {

    withChangeSetRepository { repository =>

      repository.saveNodeChange(nodeChange(11, 1, 1001))
      repository.saveNodeChange(nodeChange(12, 1, 1001))
      repository.saveNodeChange(nodeChange(13, 1, 1001))
      repository.saveNodeChange(nodeChange(14, 1, 1002))

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

      repository.changesFilter(None, None, None, None) should matchTo(
        ChangesFilter(
          Seq(
            ChangesFilterPeriod(
              name = "2016",
              totalCount = 2,
              impactedCount = 2,
              current = false,
              selected = true,
              Seq(
                ChangesFilterPeriod(
                  name = "02",
                  totalCount = 1,
                  impactedCount = 1
                ),
                ChangesFilterPeriod(
                  name = "01",
                  totalCount = 1,
                  impactedCount = 1
                )
              )
            ),
            ChangesFilterPeriod(
              name = "2015",
              totalCount = 3,
              impactedCount = 3
            )
          )
        )
      )

      repository.changesFilter(Some(Subset.nlHiking), None, None, None) should matchTo(
        ChangesFilter(
          Seq(
            ChangesFilterPeriod(
              name = "2015",
              totalCount = 2,
              impactedCount = 2,
              current = false,
              selected = true,
              Seq(
                ChangesFilterPeriod(
                  name = "02",
                  totalCount = 1,
                  impactedCount = 1
                ),
                ChangesFilterPeriod(
                  name = "01",
                  totalCount = 1,
                  impactedCount = 1
                )
              )
            )
          )
        )
      )

      repository.changesFilter(Some(Subset.beHiking), Some("2016"), None, None) should matchTo(
        ChangesFilter(
          Seq(
            ChangesFilterPeriod(
              name = "2016",
              totalCount = 2,
              impactedCount = 2,
              current = true,
              selected = true,
              Seq(
                ChangesFilterPeriod(
                  name = "02",
                  totalCount = 1,
                  impactedCount = 1
                ),
                ChangesFilterPeriod(
                  name = "01",
                  totalCount = 1,
                  impactedCount = 1
                )
              )
            ),
            ChangesFilterPeriod(
              name = "2015",
              totalCount = 1,
              impactedCount = 1
            )
          )
        )
      )

      repository.changesFilter(Some(Subset.beHiking), Some("2016"), Some("01"), None) should matchTo(
        ChangesFilter(
          Seq(
            ChangesFilterPeriod(
              name = "2016",
              totalCount = 2,
              impactedCount = 2,
              current = false,
              selected = true,
              Seq(
                ChangesFilterPeriod(
                  name = "02",
                  totalCount = 1,
                  impactedCount = 1
                ),
                ChangesFilterPeriod(
                  name = "01",
                  totalCount = 1,
                  impactedCount = 1,
                  current = true,
                  selected = true,
                  Seq(
                    ChangesFilterPeriod(
                      name = "01",
                      totalCount = 1,
                      impactedCount = 1
                    )
                  )
                )
              )
            ),
            ChangesFilterPeriod(
              name = "2015",
              totalCount = 1,
              impactedCount = 1
            )
          )
        )
      )

      repository.changesFilter(Some(Subset.beHiking), Some("2016"), Some("01"), Some("01")) should equal(
        ChangesFilter(
          Seq(
            ChangesFilterPeriod(
              name = "2016",
              totalCount = 2,
              impactedCount = 2,
              current = false,
              selected = true,
              Seq(
                ChangesFilterPeriod(
                  name = "02",
                  totalCount = 1,
                  impactedCount = 1
                ),
                ChangesFilterPeriod(
                  name = "01",
                  totalCount = 1,
                  impactedCount = 1,
                  current = false,
                  selected = true,
                  Seq(
                    ChangesFilterPeriod(
                      name = "01",
                      totalCount = 1,
                      impactedCount = 1,
                      current = true,
                      selected = true
                    )
                  )
                )
              )
            ),
            ChangesFilterPeriod(
              name = "2015",
              totalCount = 1,
              impactedCount = 1
            )
          )
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
      RefChanges(),
      RefChanges(),
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
    withCouchDatabase { database =>
      val repository: ChangeSetRepository = new ChangeSetRepositoryImpl(null, null)
      f(repository)
    }
  }
}
