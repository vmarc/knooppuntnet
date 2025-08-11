package kpn.server.analyzer.engine.changes

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NetworkChanges
import kpn.api.common.ReplicationId
import kpn.api.common.RouteType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.custom.Subset
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSet
import kpn.core.test.TestObjects.newChangeSetNetwork
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.core.test.TestObjects.newNetworkChange
import kpn.core.test.TestObjects.newNodeChange
import kpn.core.test.TestObjects.newRouteChange
import kpn.core.test.TestObjects.newRouteData
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkInfoRepository
import org.scalamock.scalatest.MockFactory

class ChangeSaverTest extends UnitTest with MockFactory {

  test("nothing to save") {

    val changeSetRepository = stub[ChangeSetRepository]
    val networkInfoRepository = stub[NetworkInfoRepository]

    val context = ChangeSetContext(
      ReplicationId(1, 2, 3),
      newChangeSet(),
      ElementIds(),
      ChangeSetChanges()
    )

    new ChangeSaver(changeSetRepository, networkInfoRepository).save(context)

    changeSetRepository.saveNetworkChange.verify(*).never()
    changeSetRepository.saveRouteChange.verify(*).never()
    changeSetRepository.saveNodeChange.verify(*).never()
    changeSetRepository.saveChangeSetSummary.verify(*).never()
  }

  test("save network changes") {

    pendingRedesign()

    val networkChange = newNetworkChange(newChangeKey(elementId = 1))

    val changeSetChanges = ChangeSetChanges(
      networkChanges = Seq(networkChange)
    )

    val changeSetRepository = stub[ChangeSetRepository]
    val networkInfoRepository = stub[NetworkInfoRepository]

    save(changeSetRepository, networkInfoRepository, changeSetChanges)

    changeSetRepository.saveRouteChange.verify(*).never()
    changeSetRepository.saveNodeChange.verify(*).never()

    changeSetRepository.saveNetworkChange.verify(
      where { (savedNetworkChange: NetworkChange) =>
        assertEqual(savedNetworkChange, networkChange)
        true
      }
    ).once()

    changeSetRepository.saveChangeSetSummary.verify(
      where { (changeSetSummary: ChangeSetSummary) =>
        assertEqual(
          changeSetSummary,
          newChangeSetSummary(
            networkChanges = NetworkChanges(
              updates = Seq(
                newChangeSetNetwork()
              )
            )
          )
        )
        true
      }
    ).once()

    networkInfoRepository.updateNetworkChangeCount.verify(
      where { (networkId: Long) =>
        networkId should equal(1)
        true
      }
    ).once()
  }

  test("save route changes") {

    val routeChange = newRouteChange(
      newChangeKey(elementId = 10),
      after = Some(
        newRouteData(
          countries = Seq(Country.nl),
          routeTypes = Seq(RouteType.hiking)
        )
      ),
      facts = Seq(Fact.OrphanRoute)
    )

    val changeSetChanges = ChangeSetChanges(
      routeChanges = Seq(routeChange)
    )

    val changeSetRepository = stub[ChangeSetRepository]
    val networkInfoRepository = stub[NetworkInfoRepository]

    save(changeSetRepository, networkInfoRepository, changeSetChanges)

    changeSetRepository.saveNetworkChange.verify(*).never()
    changeSetRepository.saveNodeChange.verify(*).never()
    networkInfoRepository.updateNetworkChangeCount.verify(*).never()

    changeSetRepository.saveRouteChange.verify(
      where { (savedRouteChange: RouteChange) =>
        assertEqual(savedRouteChange, routeChange)
        true
      }
    ).once()

    changeSetRepository.saveChangeSetSummary.verify(
      where { (changeSetSummary: ChangeSetSummary) =>
        assertEqual(
          changeSetSummary,
          newChangeSetSummary(
            subsets = Seq(Subset.nlHiking),
            orphanRouteChanges = Seq(
              ChangeSetSubsetElementRefs(
                Subset.nlHiking,
                ChangeSetElementRefs(
                  added = Seq(
                    ChangeSetElementRef(10, "", happy = false, investigate = false)
                  )
                )
              )
            ),
            subsetAnalyses = Seq(
              ChangeSetSubsetAnalysis(Subset.nlHiking)
            )
          )
        )
        true
      }
    ).once()
  }

  test("save node changes") {

    val nodeChange = newNodeChange(
      newChangeKey(elementId = 1001),
      subsets = Seq(Subset.nlHiking),
      name = Some("01"),
      facts = Seq(Fact.OrphanNode)
    )

    val changeSetChanges = ChangeSetChanges(
      nodeChanges = Seq(nodeChange)
    )

    val changeSetRepository = stub[ChangeSetRepository]
    val networkInfoRepository = stub[NetworkInfoRepository]

    save(changeSetRepository, networkInfoRepository, changeSetChanges)

    changeSetRepository.saveNetworkChange.verify(*).never()
    changeSetRepository.saveRouteChange.verify(*).never()
    networkInfoRepository.updateNetworkChangeCount.verify(*).never()

    changeSetRepository.saveNodeChange.verify(
      where { (savedNodeChange: NodeChange) =>
        assertEqual(savedNodeChange, nodeChange)
        true
      }
    ).once()

    changeSetRepository.saveChangeSetSummary.verify(
      where { (changeSetSummary: ChangeSetSummary) =>
        assertEqual(
          changeSetSummary,
          newChangeSetSummary(
            subsets = Seq(Subset.nlHiking),
            orphanNodeChanges = Seq(
              ChangeSetSubsetElementRefs(
                Subset.nlHiking,
                ChangeSetElementRefs(
                  updated = Seq(
                    ChangeSetElementRef(1001, "01", happy = false, investigate = false)
                  )
                )
              )
            ),
            subsetAnalyses = Seq(
              ChangeSetSubsetAnalysis(Subset.nlHiking)
            )
          )
        )
        true
      }
    ).once()
  }

  private def save(
    changeSetRepository: ChangeSetRepository,
    networkInfoRepository: NetworkInfoRepository,
    changeSetChanges: ChangeSetChanges
  ): Unit = {
    val context = ChangeSetContext(
      ReplicationId(0, 0, 1),
      newChangeSet(),
      ElementIds(),
      changeSetChanges
    )
    new ChangeSaver(changeSetRepository, networkInfoRepository).save(context)
  }
}
