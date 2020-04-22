package kpn.server.api.analysis.pages

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetNetwork
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.details.ChangeKey
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.util.UnitTest

class ChangeSetSummarySubsetFilterTest extends UnitTest {

  test("filter information in ChangeSetSummary so that only information for given 'subset' remains") {

    val changeSetSummary: ChangeSetSummary = buildChangeSetSummary(
      networkChanges = NetworkChanges(
        creates = Seq(
          changeSetNetwork("network-1", Subset.nlHiking, happy = true, investigate = true),
          changeSetNetwork("network-2", Subset.beHiking, happy = false, investigate = false)
        ),
        updates = Seq(
          changeSetNetwork("network-3", Subset.nlHiking, happy = true, investigate = true),
          changeSetNetwork("network-4", Subset.beHiking, happy = false, investigate = false)
        ),
        deletes = Seq(
          changeSetNetwork("network-5", Subset.nlHiking, happy = true, investigate = true),
          changeSetNetwork("network-6", Subset.beHiking, happy = false, investigate = false)
        )
      ),
      orphanRouteChanges = Seq(
        ChangeSetSubsetElementRefs(
          Subset.nlHiking,
          ChangeSetElementRefs()
        ),
        ChangeSetSubsetElementRefs(
          Subset.beHiking,
          ChangeSetElementRefs()
        )
      ),
      orphanNodeChanges = Seq(
        ChangeSetSubsetElementRefs(
          Subset.nlHiking,
          ChangeSetElementRefs()
        ),
        ChangeSetSubsetElementRefs(
          Subset.beHiking,
          ChangeSetElementRefs()
        )
      )
    )

    changeSetSummary.happy should equal(true)
    changeSetSummary.investigate should equal(true)

    val nlHikingChangeSetSummary = ChangeSetSummarySubsetFilter.filter(changeSetSummary, Subset.nlHiking)

    nlHikingChangeSetSummary should equal(
      buildChangeSetSummary(
        networkChanges = NetworkChanges(
          creates = Seq(
            changeSetNetwork("network-1", Subset.nlHiking, happy = true, investigate = true)
          ),
          updates = Seq(
            changeSetNetwork("network-3", Subset.nlHiking, happy = true, investigate = true)
          ),
          deletes = Seq(
            changeSetNetwork("network-5", Subset.nlHiking, happy = true, investigate = true)
          )
        ),
        orphanRouteChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs()
          )
        ),
        orphanNodeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs()
          )
        )
      )
    )

    nlHikingChangeSetSummary.happy should equal(true)
    nlHikingChangeSetSummary.investigate should equal(true)

    val beHikingChangeSetSummary = ChangeSetSummarySubsetFilter.filter(changeSetSummary, Subset.beHiking)

    beHikingChangeSetSummary should equal(
      buildChangeSetSummary(
        networkChanges = NetworkChanges(
          creates = Seq(
            changeSetNetwork("network-2", Subset.beHiking, happy = false, investigate = false)
          ),
          updates = Seq(
            changeSetNetwork("network-4", Subset.beHiking, happy = false, investigate = false)
          ),
          deletes = Seq(
            changeSetNetwork("network-6", Subset.beHiking, happy = false, investigate = false)
          )
        ),
        orphanRouteChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.beHiking,
            ChangeSetElementRefs()
          )
        ),
        orphanNodeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.beHiking,
            ChangeSetElementRefs()
          )
        )
      )
    )

    beHikingChangeSetSummary.happy should equal(false)
    beHikingChangeSetSummary.investigate should equal(false)
  }

  private def buildChangeSetSummary(
    networkChanges: NetworkChanges,
    orphanRouteChanges: Seq[ChangeSetSubsetElementRefs],
    orphanNodeChanges: Seq[ChangeSetSubsetElementRefs]
  ): ChangeSetSummary = {
    ChangeSetSummary(
      key = ChangeKey(
        replicationNumber = 1,
        timestamp = Timestamp(2015, 8, 11),
        changeSetId = 1,
        elementId = 0
      ),
      timestampFrom = Timestamp(2015, 8, 11),
      timestampUntil = Timestamp(2015, 8, 11),
      networkChanges,
      orphanRouteChanges,
      orphanNodeChanges
    )
  }

  private def changeSetNetwork(networkName: String, subset: Subset, happy: Boolean, investigate: Boolean): ChangeSetNetwork = {
    ChangeSetNetwork(
      country = Some(subset.country),
      networkType = subset.networkType,
      networkId = 1,
      networkName = networkName,
      routeChanges = ChangeSetElementRefs(),
      nodeChanges = ChangeSetElementRefs(),
      happy = happy,
      investigate = investigate
    )
  }
}
