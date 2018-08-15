package kpn.core.engine.changes.integration

import kpn.core.test.TestData2
import kpn.shared.ChangeSetElementRefs
import kpn.shared.ChangeSetSummary
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NetworkChanges
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.changes.ChangeAction
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.RefChanges
import kpn.shared.common.Ref
import kpn.shared.data.raw.RawMember
import kpn.shared.diff.NetworkData
import kpn.shared.diff.NetworkDataUpdate
import kpn.shared.diff.RefDiffs

class NetworkUpdateNodeTest08 extends AbstractTest {

  test("network update - an orphan node that is added to the network is no longer orphan") {

    val dataBefore = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02") // orphan node, not referenced by the network
      .networkRelation(
      1,
      "name",
      Seq(
        newMember("node", 1001)
        // the network does not reference the orphan node
      )
    )
      .data

    val dataAfter = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("node", 1001),
          newMember("node", 1002) // reference to the previous orphan node
        )
      )
      .data

    val tc = new TestConfig()
    tc.watchOrphanNode(1002)
    tc.relationBefore(dataBefore, 1)
    tc.watchNetwork(dataBefore, 1)
    tc.relationAfter(dataAfter, 1)
    tc.nodesBefore(dataBefore, 1002)

    // before:
    tc.analysisData.networks.watched.isReferencingNode(1002) should equal(false)
    tc.analysisData.orphanNodes.watched.contains(1002) should equal(true)
    tc.analysisData.orphanNodes.ignored.contains(1002) should equal(false)

    // act:
    tc.process(ChangeAction.Modify, relation(dataAfter, 1))

    // after:
    tc.analysisData.networks.watched.isReferencingNode(1002) should equal(true)
    tc.analysisData.orphanNodes.watched.contains(1002) should equal(false)
    tc.analysisData.orphanNodes.ignored.contains(1002) should equal(false)

    (tc.analysisRepository.saveNetwork _).verify(*).once()

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { (changeSetSummary: ChangeSetSummary) =>
        changeSetSummary should equal(
          newChangeSetSummary(
            subsets = Seq(Subset.nlHiking),
            networkChanges = NetworkChanges(
              updates = Seq(
                newChangeSetNetwork(
                  Some(Country.nl),
                  NetworkType.hiking,
                  1,
                  "name",
                  nodeChanges = ChangeSetElementRefs(
                    added = Seq(newChangeSetElementRef(1002, "02", happy = true))
                  ),
                  happy = true
                )
              )
            ),
            happy = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveNetworkChange _).verify(
      where { (networkChange: NetworkChange) =>
        networkChange should equal(
          newNetworkChange(
            newChangeKey(elementId = 1),
            ChangeType.Update,
            Some(Country.nl),
            NetworkType.hiking,
            1,
            "name",
            orphanNodes = RefChanges(
              oldRefs = Seq(Ref(1002, "02"))
            ),
            networkDataUpdate = Some(
              NetworkDataUpdate(
                NetworkData(
                  newRawRelation(
                    1,
                    members = Seq(
                      RawMember("node", 1001, None)
                    ),
                    tags = newNetworkTags()
                  ),
                  "name"
                ),
                NetworkData(
                  newRawRelation(
                    1,
                    members = Seq(
                      RawMember("node", 1001, None),
                      RawMember("node", 1002, None)
                    ),
                    tags = newNetworkTags()
                  ),
                  "name"
                )
              )
            ),
            networkNodes = RefDiffs(
              added = Seq(Ref(1002, "02"))
            ),
            happy = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveNodeChange _).verify(
      where { (nodeChange: NodeChange) =>
        nodeChange should equal(
          newNodeChange(
            newChangeKey(elementId = 1002),
            ChangeType.Update,
            Seq(Subset.nlHiking),
            "02",
            before = Some(
              newRawNodeWithName(1002, "02")
            ),
            after = Some(
              newRawNodeWithName(1002, "02")
            ),
            addedToNetwork = Seq(Ref(1, "name")),
            facts = Seq(Fact.WasOrphan)
          )
        )
        true
      }
    )
  }

}
