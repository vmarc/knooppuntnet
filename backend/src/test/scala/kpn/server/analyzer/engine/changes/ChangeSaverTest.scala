package kpn.server.analyzer.engine.changes

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NetworkChanges
import kpn.api.common.ReplicationId
import kpn.api.common.RouteType
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
import kpn.server.analyzer.engine.context.ChangeElementIds
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkInfoRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.scalamock.stubs.Stubs

class ChangeSaverTest extends UnitTest with Stubs {

  test("nothing to save") {

    // setup
    val changeSetRepository = stub[ChangeSetRepository]
    (changeSetRepository.saveNetworkChange _).returnsWith(())
    (changeSetRepository.saveRouteChange _).returnsWith(())
    (changeSetRepository.saveNodeChange _).returnsWith(())
    (changeSetRepository.saveChangeSetSummary _).returnsWith(())

    val networkInfoRepository = stub[NetworkInfoRepository]

    val context = ChangeSetContext(
      ReplicationId(1, 2, 3),
      newChangeSet(),
      ChangeElementIds(),
      ChangeSetChanges()
    )

    // execute
    new ChangeSaver(changeSetRepository, networkInfoRepository).save(context)

    // verify
    (changeSetRepository.saveNetworkChange _).times should equal(0)
    (changeSetRepository.saveRouteChange _).times should equal(0)
    (changeSetRepository.saveNodeChange _).times should equal(0)
    (changeSetRepository.saveChangeSetSummary _).times should equal(0)
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

    (changeSetRepository.saveRouteChange _).times should equal(0)
    (changeSetRepository.saveNodeChange _).times should equal(0)

    (changeSetRepository.saveNetworkChange _).calls should equal(Seq(networkChange))

    assertEquals(
      (changeSetRepository.saveChangeSetSummary _).calls,
      Seq(
        newChangeSetSummary(
          networkChanges = NetworkChanges(
            updates = Seq(
              newChangeSetNetwork()
            )
          )
        )
      )
    )

    (networkInfoRepository.updateNetworkChangeCount _).calls should equal(Seq(1))
  }

  test("save route changes") {

    // setup
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
    (changeSetRepository.saveNodeChange _).returnsWith(())
    (changeSetRepository.saveRouteChange _).returnsWith(())
    (changeSetRepository.saveNetworkChange _).returnsWith(())
    (changeSetRepository.saveChangeSetSummary _).returnsWith(())

    val networkInfoRepository = stub[NetworkInfoRepository]
    (networkInfoRepository.updateNetworkChangeCount _).returnsWith(())

    // execute
    save(changeSetRepository, networkInfoRepository, changeSetChanges)

    // verify
    (changeSetRepository.saveNetworkChange _).times should equal(0)
    (changeSetRepository.saveNodeChange _).times should equal(0)
    (networkInfoRepository.updateNetworkChangeCount _).times should equal(0)

    assertEqual(
      (changeSetRepository.saveRouteChange _).calls,
      Seq(routeChange)
    )

    assertEqual(
      (changeSetRepository.saveChangeSetSummary _).calls,
      Seq(
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
    )
  }

  test("save node changes") {

    // setup
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
    (changeSetRepository.saveNodeChange _).returnsWith(())
    (changeSetRepository.saveRouteChange _).returnsWith(())
    (changeSetRepository.saveNetworkChange _).returnsWith(())
    (changeSetRepository.saveChangeSetSummary _).returnsWith(())

    val networkInfoRepository = stub[NetworkInfoRepository]
    (networkInfoRepository.updateNetworkChangeCount _).returnsWith(())

    // execute
    save(changeSetRepository, networkInfoRepository, changeSetChanges)

    // verify
    (changeSetRepository.saveNetworkChange _).times should equal(0)
    (changeSetRepository.saveRouteChange _).times should equal(0)
    (networkInfoRepository.updateNetworkChangeCount _).times should equal(0)

    assertEquals(
      (changeSetRepository.saveNodeChange _).calls,
      Seq(nodeChange)
    )

    assertEquals(
      (changeSetRepository.saveChangeSetSummary _).calls,
      Seq(
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
    )
  }

  private def save(
    changeSetRepository: ChangeSetRepository,
    networkInfoRepository: NetworkInfoRepository,
    changeSetChanges: ChangeSetChanges
  ): Unit = {
    val context = ChangeSetContext(
      ReplicationId(0, 0, 1),
      newChangeSet(),
      ChangeElementIds(),
      changeSetChanges
    )
    new ChangeSaver(changeSetRepository, networkInfoRepository).save(context)
  }
}
