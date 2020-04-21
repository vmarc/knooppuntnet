package kpn.server.analyzer.engine.changes

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.NetworkChanges
import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.repository.ChangeSetRepository
import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ChangeSaverTest extends AnyFunSuite with Matchers with MockFactory with SharedTestObjects {

  test("nothing to save") {

    val changeSetRepository = stub[ChangeSetRepository]

    new ChangeSaverImpl(
      changeSetRepository
    ).save(
      ReplicationId(1, 2, 3),
      newChangeSet(),
      ChangeSetChanges()
    )

    (changeSetRepository.saveNetworkChange _).verify(*).never()
    (changeSetRepository.saveRouteChange _).verify(*).never()
    (changeSetRepository.saveNodeChange _).verify(*).never()
    (changeSetRepository.saveChangeSetSummary _).verify(*).never()
  }

  test("save network changes") {

    val networkChange = newNetworkChange(newChangeKey(elementId = 1))

    val changeSetChanges = ChangeSetChanges(
      networkChanges = Seq(networkChange)
    )

    val changeSetRepository = stub[ChangeSetRepository]

    save(changeSetRepository, changeSetChanges)

    (changeSetRepository.saveRouteChange _).verify(*).never()
    (changeSetRepository.saveNodeChange _).verify(*).never()

    (changeSetRepository.saveNetworkChange _).verify(
      where { savedNetworkChange: NetworkChange =>
        savedNetworkChange should equal(networkChange)
        true
      }
    ).once()

    (changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary should equal(
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

    save(changeSetRepository, changeSetChanges)

    (changeSetRepository.saveNetworkChange _).verify(*).never()
    (changeSetRepository.saveNodeChange _).verify(*).never()

    (changeSetRepository.saveRouteChange _).verify(
      where { savedRouteChange: RouteChange =>
        savedRouteChange should equal(routeChange)
        true
      }
    ).once()

    (changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary should equal(
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

    save(changeSetRepository, changeSetChanges)

    (changeSetRepository.saveNetworkChange _).verify(*).never()
    (changeSetRepository.saveRouteChange _).verify(*).never()

    (changeSetRepository.saveNodeChange _).verify(
      where { savedNodeChange: NodeChange =>
        savedNodeChange should equal(nodeChange)
        true
      }
    ).once()

    (changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary should equal(
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

  private def save(changeSetRepository: ChangeSetRepository, changeSetChanges: ChangeSetChanges): Unit = {
    new ChangeSaverImpl(
      changeSetRepository
    ).save(
      ReplicationId(0, 0, 1),
      newChangeSet(),
      changeSetChanges
    )
  }
}
