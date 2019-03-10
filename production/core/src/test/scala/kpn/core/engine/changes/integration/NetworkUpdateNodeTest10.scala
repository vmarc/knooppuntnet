package kpn.core.engine.changes.integration

import kpn.core.test.TestData2
import kpn.shared.ChangeSetElementRefs
import kpn.shared.ChangeSetSummary
import kpn.shared.Country
import kpn.shared.NetworkChanges
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.changes.ChangeAction
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.RefBooleanChange
import kpn.shared.common.Ref
import kpn.shared.data.raw.RawMember
import kpn.shared.diff.NetworkData
import kpn.shared.diff.NetworkDataUpdate
import kpn.shared.diff.RefDiffs

class NetworkUpdateNodeTest10 extends AbstractTest {

  test("network update - node role 'connection' added") {

    val dataBefore = TestData2()
      .networkNode(1001, "01")
      .networkRelation(
        1,
        "network-name",
        Seq(
          newMember("node", 1001)
        )
      )
      .data

    val dataAfter = TestData2()
      .networkNode(1001, "01")
      .networkRelation(
        1,
        "network-name",
        Seq(
          newMember("node", 1001, "connection")
        )
      )
      .data

    val tc = new TestConfig()
    tc.watchNetwork(dataBefore, 1)
    tc.relationBefore(dataBefore, 1)
    tc.relationAfter(dataAfter, 1)

    tc.process(ChangeAction.Modify, relation(dataAfter, 1))

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary should equal(
          newChangeSetSummary(
            subsets = Seq(Subset.nlHiking),
            networkChanges = NetworkChanges(
              updates = Seq(
                newChangeSetNetwork(
                  Some(Country.nl),
                  NetworkType.hiking,
                  1,
                  "network-name",
                  nodeChanges = ChangeSetElementRefs(
                    updated = Seq(newChangeSetElementRef(1001, "01"))
                  )
                )
              )
            )
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveNetworkChange _).verify(
      where { networkChange: NetworkChange =>
        networkChange should equal(
          newNetworkChange(
            newChangeKey(elementId = 1),
            ChangeType.Update,
            Some(Country.nl),
            NetworkType.hiking,
            1,
            "network-name",
            networkDataUpdate = Some(
              NetworkDataUpdate(
                NetworkData(
                  newRawRelation(
                    1,
                    members = Seq(
                      RawMember("node", 1001, None)
                    ),
                    tags = newNetworkTags("network-name")
                  ),
                  "network-name"
                ),
                NetworkData(
                  newRawRelation(
                    1,
                    members = Seq(
                      RawMember("node", 1001, Some("connection"))
                    ),
                    tags = newNetworkTags("network-name")
                  ),
                  "network-name"
                )
              )
            ),
            networkNodes = RefDiffs(
              updated = Seq(
                Ref(1001, "01")
              )
            )
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveNodeChange _).verify(
      where { nodeChange: NodeChange =>
        nodeChange should equal(
          newNodeChange(
            newChangeKey(elementId = 1001),
            ChangeType.Update,
            Seq(Subset.nlHiking),
            "01",
            before = Some(
              newRawNodeWithName(1001, "01")
            ),
            after = Some(
              newRawNodeWithName(1001, "01")
            ),
            roleConnectionChanges = Seq(
              RefBooleanChange(Ref(1, "network-name"), after = true)
            )
          )
        )
        true
      }
    )
  }

}
