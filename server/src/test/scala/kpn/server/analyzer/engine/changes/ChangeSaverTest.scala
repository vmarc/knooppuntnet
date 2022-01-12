package kpn.server.analyzer.engine.changes

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.NetworkChanges
import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkInfoRepository
import org.scalamock.scalatest.MockFactory

class ChangeSaverTest extends UnitTest with MockFactory with SharedTestObjects {

  test("nothing to save") {

    val changeSetRepository = stub[ChangeSetRepository]
    val networkInfoRepository = stub[NetworkInfoRepository]

    val context = ChangeSetContext(
      ReplicationId(1, 2, 3),
      newChangeSet(),
      ElementIds(),
      ChangeSetChanges()
    )

    new ChangeSaverImpl(changeSetRepository, networkInfoRepository).save(context)

    (changeSetRepository.saveNetworkInfoChange _).verify(*).never()
    (changeSetRepository.saveRouteChange _).verify(*).never()
    (changeSetRepository.saveNodeChange _).verify(*).never()
    (changeSetRepository.saveChangeSetSummary _).verify(*).never()
  }

  test("save network changes") {

    val networkChange = newNetworkInfoChange(newChangeKey(elementId = 1))

    val changeSetChanges = ChangeSetChanges(
      networkInfoChanges = Seq(networkChange)
    )

    val changeSetRepository = stub[ChangeSetRepository]
    val networkInfoRepository = stub[NetworkInfoRepository]

    save(changeSetRepository, networkInfoRepository, changeSetChanges)

    (changeSetRepository.saveRouteChange _).verify(*).never()
    (changeSetRepository.saveNodeChange _).verify(*).never()

    (changeSetRepository.saveNetworkInfoChange _).verify(
      where { savedNetworkChange: NetworkInfoChange =>
        savedNetworkChange.shouldMatchTo(networkChange)
        true
      }
    ).once()

    (changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary.shouldMatchTo(
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

    (networkInfoRepository.updateNetworkChangeCount _).verify(
      where { networkId: Long =>
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
          country = Some(Country.nl),
          networkType = NetworkType.hiking
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

    (changeSetRepository.saveNetworkInfoChange _).verify(*).never()
    (changeSetRepository.saveNodeChange _).verify(*).never()
    (networkInfoRepository.updateNetworkChangeCount _).verify(*).never()

    (changeSetRepository.saveRouteChange _).verify(
      where { savedRouteChange: RouteChange =>
        savedRouteChange.shouldMatchTo(routeChange)
        true
      }
    ).once()

    (changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary.shouldMatchTo(
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
      name = "01",
      facts = Seq(Fact.OrphanNode)
    )

    val changeSetChanges = ChangeSetChanges(
      nodeChanges = Seq(nodeChange)
    )

    val changeSetRepository = stub[ChangeSetRepository]
    val networkInfoRepository = stub[NetworkInfoRepository]

    save(changeSetRepository, networkInfoRepository, changeSetChanges)

    (changeSetRepository.saveNetworkInfoChange _).verify(*).never()
    (changeSetRepository.saveRouteChange _).verify(*).never()
    (networkInfoRepository.updateNetworkChangeCount _).verify(*).never()

    (changeSetRepository.saveNodeChange _).verify(
      where { savedNodeChange: NodeChange =>
        savedNodeChange.shouldMatchTo(nodeChange)
        true
      }
    ).once()

    (changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary.shouldMatchTo(
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
    new ChangeSaverImpl(changeSetRepository, networkInfoRepository).save(context)
  }
}
