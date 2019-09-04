package kpn.core.engine.changes

import kpn.core.engine.changes.data.ChangeSetChanges
import kpn.core.repository.ChangeSetRepository
import kpn.shared.ChangeSetElementRef
import kpn.shared.ChangeSetElementRefs
import kpn.shared.ChangeSetSubsetElementRefs
import kpn.shared.ChangeSetSummary
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NetworkChanges
import kpn.shared.NetworkType
import kpn.shared.ReplicationId
import kpn.shared.SharedTestObjects
import kpn.shared.Subset
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.RouteChange
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ChangeSaverTest extends FunSuite with Matchers with MockFactory with SharedTestObjects {

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
