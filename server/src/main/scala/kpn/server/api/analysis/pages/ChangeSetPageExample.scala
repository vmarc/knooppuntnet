package kpn.server.api.analysis.pages

import kpn.shared.Bounds
import kpn.shared.ChangeSetElementRef
import kpn.shared.ChangeSetElementRefs
import kpn.shared.ChangeSetSubsetElementRefs
import kpn.shared.ChangeSetSummary
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.LatLonImpl
import kpn.shared.NetworkChanges
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.Timestamp
import kpn.shared.changes.ChangeSetInfo
import kpn.shared.changes.ChangeSetPage
import kpn.shared.changes.details.ChangeKey
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChangeInfo
import kpn.shared.changes.details.RefBooleanChange
import kpn.shared.changes.details.RefChanges
import kpn.shared.common.KnownElements
import kpn.shared.common.Ref
import kpn.shared.data.MetaData
import kpn.shared.data.Tags
import kpn.shared.diff.IdDiffs
import kpn.shared.diff.RefDiffs
import kpn.shared.diff.TagDiffs
import kpn.shared.diff.common.FactDiffs
import kpn.shared.diff.node.NodeMoved
import kpn.shared.diff.route.RouteDiff
import kpn.shared.node.NodeChangeInfo
import kpn.shared.route.GeometryDiff
import kpn.shared.route.RouteChangeInfo

object ChangeSetPageExample {

  private val nodeId1 = 1001L
  private val nodeId2 = 1002L
  private val nodeId3 = 1003L
  private val nodeId4 = 1004L

  private val routeId1 = 11L
  private val routeId2 = 12L
  private val routeId3 = 13L
  private val routeId4 = 14L

  val page: ChangeSetPage = {
    ChangeSetPage(
      summary = changeSetSummary(),
      changeSetInfo = Some(changeSetInfo()),
      networkChanges = networkChanges(),
      routeChanges = routeChanges(),
      nodeChanges = nodeChanges(),
      knownElements = KnownElements(
        nodeIds = Set(nodeId1, nodeId2), // no link in page for any other node
        routeIds = Set(routeId1, routeId2) // no link in page for any other route
      )
    )
  }

  private def changeSetSummary(): ChangeSetSummary = {
    ChangeSetSummary(
      key = ChangeKey(
        replicationNumber = 1,
        timestamp = Timestamp(2015, 1, 2),
        changeSetId = 1,
        elementId = 1
      ),
      subsets = Subset.all,
      timestampFrom = Timestamp(2015, 1, 2),
      timestampUntil = Timestamp(2015, 1, 2),
      networkChanges = NetworkChanges(), // TODO this is not used by the application; can be left out???
      //        networkChanges = NetworkChanges(
      //          creates = Seq(
      //            ChangeSetNetwork(
      //              country = Some(Country.nl),
      //              networkType = NetworkType.hiking,
      //              networkId = 1,
      //              networkName = "network one",
      //              routeChanges = ChangeSetElementRefs(
      //                removed = Seq(
      //                  ChangeSetElementRef(
      //                    id = routeId1,
      //                    name = "01-02",
      //                    happy = true,
      //                    investigate = true
      //                  ),
      //                  ChangeSetElementRef(
      //                    id = routeId2,
      //                    name = "02-03",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                ),
      //                added = Seq(
      //                  ChangeSetElementRef(
      //                    id = routeId3,
      //                    name = "03-04",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                ),
      //                updated = Seq(
      //                  ChangeSetElementRef(
      //                    id = routeId4,
      //                    name = "04-05",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                )
      //              ),
      //              nodeChanges = ChangeSetElementRefs(
      //                removed = Seq(
      //                  ChangeSetElementRef(
      //                    id = nodeId1,
      //                    name = "01",
      //                    happy = true,
      //                    investigate = true
      //                  ),
      //                  ChangeSetElementRef(
      //                    id = nodeId2,
      //                    name = "02",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                ),
      //                added = Seq(
      //                  ChangeSetElementRef(
      //                    id = nodeId3,
      //                    name = "03",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                ),
      //                updated = Seq(
      //                  ChangeSetElementRef(
      //                    id = nodeId4,
      //                    name = "04",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                )
      //              ),
      //              happy = true,
      //              investigate = true
      //            )
      //          ),
      //          updates = Seq(
      //            ChangeSetNetwork(
      //              country = Some(Country.nl),
      //              networkType = NetworkType.hiking,
      //              networkId = 2,
      //              networkName = "network two",
      //              routeChanges = ChangeSetElementRefs(
      //                removed = Seq(
      //                  ChangeSetElementRef(
      //                    id = routeId1,
      //                    name = "01-02",
      //                    happy = true,
      //                    investigate = true
      //                  ),
      //                  ChangeSetElementRef(
      //                    id = routeId2,
      //                    name = "02-03",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                ),
      //                added = Seq(
      //                  ChangeSetElementRef(
      //                    id = routeId3,
      //                    name = "03-04",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                ),
      //                updated = Seq(
      //                  ChangeSetElementRef(
      //                    id = routeId4,
      //                    name = "04-05",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                )
      //              ),
      //              nodeChanges = ChangeSetElementRefs(
      //                removed = Seq(
      //                  ChangeSetElementRef(
      //                    id = nodeId1,
      //                    name = "01",
      //                    happy = true,
      //                    investigate = true
      //                  ),
      //                  ChangeSetElementRef(
      //                    id = nodeId2,
      //                    name = "02",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                ),
      //                added = Seq(
      //                  ChangeSetElementRef(
      //                    id = nodeId3,
      //                    name = "03",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                ),
      //                updated = Seq(
      //                  ChangeSetElementRef(
      //                    id = nodeId4,
      //                    name = "04",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                )
      //              ),
      //              happy = true,
      //              investigate = true
      //            )
      //          ),
      //          deletes = Seq(
      //            ChangeSetNetwork(
      //              country = Some(Country.nl),
      //              networkType = NetworkType.hiking,
      //              networkId = 3,
      //              networkName = "network three",
      //              routeChanges = ChangeSetElementRefs(
      //                removed = Seq(
      //                  ChangeSetElementRef(
      //                    id = routeId1,
      //                    name = "01-02",
      //                    happy = true,
      //                    investigate = true
      //                  ),
      //                  ChangeSetElementRef(
      //                    id = routeId2,
      //                    name = "02-03",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                ),
      //                added = Seq(
      //                  ChangeSetElementRef(
      //                    id = routeId3,
      //                    name = "03-04",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                ),
      //                updated = Seq(
      //                  ChangeSetElementRef(
      //                    id = routeId4,
      //                    name = "04-05",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                )
      //              ),
      //              nodeChanges = ChangeSetElementRefs(
      //                removed = Seq(
      //                  ChangeSetElementRef(
      //                    id = nodeId1,
      //                    name = "01",
      //                    happy = true,
      //                    investigate = true
      //                  ),
      //                  ChangeSetElementRef(
      //                    id = nodeId2,
      //                    name = "02",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                ),
      //                added = Seq(
      //                  ChangeSetElementRef(
      //                    id = nodeId3,
      //                    name = "03",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                ),
      //                updated = Seq(
      //                  ChangeSetElementRef(
      //                    id = nodeId4,
      //                    name = "04",
      //                    happy = true,
      //                    investigate = true
      //                  )
      //                )
      //              ),
      //              happy = true,
      //              investigate = true
      //            )
      //          )
      //        ),
      orphanRouteChanges = Seq(
        ChangeSetSubsetElementRefs(
          subset = Subset.nlHiking,
          elementRefs = ChangeSetElementRefs(
            removed = Seq(
              ChangeSetElementRef(
                id = routeId1,
                name = "01-02",
                happy = true,
                investigate = true
              ),
              ChangeSetElementRef(
                id = routeId2,
                name = "02-03",
                happy = true,
                investigate = true
              )
            ),
            added = Seq(
              ChangeSetElementRef(
                id = routeId3,
                name = "03-04",
                happy = true,
                investigate = true
              )
            ),
            updated = Seq(
              ChangeSetElementRef(
                id = routeId4,
                name = "04-05",
                happy = true,
                investigate = true
              )
            )
          )
        )
      ),
      orphanNodeChanges = Seq(
        ChangeSetSubsetElementRefs(
          subset = Subset.beBicycle,
          elementRefs = ChangeSetElementRefs(
            removed = Seq(
              ChangeSetElementRef(
                id = nodeId1,
                name = "01",
                happy = true,
                investigate = true
              ),
              ChangeSetElementRef(
                id = nodeId2,
                name = "02",
                happy = true,
                investigate = true
              )
            ),
            added = Seq(
              ChangeSetElementRef(
                id = nodeId1,
                name = "01",
                happy = true,
                investigate = true
              ),
              ChangeSetElementRef(
                id = nodeId2,
                name = "02",
                happy = true,
                investigate = true
              ),
              ChangeSetElementRef(
                id = nodeId3,
                name = "03",
                happy = true,
                investigate = true
              )
            ),
            updated = Seq(
              ChangeSetElementRef(
                id = nodeId4,
                name = "04",
                happy = true,
                investigate = true
              )
            )
          )
        )
      ),
      happy = true,
      investigate = true
    )
  }

  private def changeSetInfo(): ChangeSetInfo = {
    ChangeSetInfo(
      id = 1,
      createdAt = Timestamp(2015, 1, 2),
      closedAt = Some(Timestamp(2015, 1, 2)),
      open = false,
      commentsCount = 1,
      tags = Tags.from("comment" -> "this is a comment")
    )
  }

  private def networkChanges(): Seq[NetworkChangeInfo] = {
    Seq(
      networkCreated(),
      networkUpdated(),
      networkDeleted()
    )
  }

  private def networkCreated(): NetworkChangeInfo = {
    NetworkChangeInfo(
      comment = None, // no need to populate here, comment already in ChangeSetInfo
      key = ChangeKey(
        replicationNumber = 1,
        timestamp = Timestamp(2015, 1, 2),
        changeSetId = 1,
        elementId = 1
      ),
      changeType = ChangeType.Create, // <==
      country = Some(Country.nl),
      networkType = NetworkType.hiking,
      networkId = 1,
      networkName = "network one",
      before = None,
      after = Some(
        MetaData(
          version = 1,
          timestamp = Timestamp(2015, 1, 2),
          changeSetId = 1
        )
      ),
      orphanRoutes = RefChanges(
        oldRefs = Seq(
          Ref(routeId1, "01-02"),
          Ref(routeId3, "03-04")
        ),
        newRefs = Seq(
          Ref(routeId3, "01-02"),
          Ref(routeId4, "02-03")
        )
      ),
      orphanNodes = RefChanges(
        oldRefs = Seq(
          Ref(nodeId1, "01"),
          Ref(nodeId3, "03")
        ),
        newRefs = Seq(
          Ref(nodeId2, "02"),
          Ref(nodeId4, "04")
        )
      ),
      networkDataUpdated = true,
      networkNodes = RefDiffs(
        removed = Seq(
          Ref(nodeId1, "01"),
          Ref(nodeId3, "03")
        ),
        added = Seq(
          Ref(nodeId1, "01"),
          Ref(nodeId3, "03")
        ),
        updated = Seq(
          Ref(nodeId1, "01"),
          Ref(nodeId3, "03")
        )
      ),
      routes = RefDiffs(
        removed = Seq(
          Ref(routeId1, "01-02"),
          Ref(routeId3, "03-04")
        ),
        added = Seq(
          Ref(routeId1, "01-02"),
          Ref(routeId3, "03-04")
        ),
        updated = Seq(
          Ref(routeId1, "01-02"),
          Ref(routeId3, "03-04")
        )
      ),
      nodes = IdDiffs(
        removed = Seq(1010, 1011, 1012),
        added = Seq(1010, 1011, 1012),
        updated = Seq(1010, 1011, 1012)
      ),
      ways = IdDiffs(
        removed = Seq(110, 111, 112),
        added = Seq(110, 111, 112),
        updated = Seq(110, 111, 112)
      ),
      relations = IdDiffs(
        removed = Seq(10, 11, 12),
        added = Seq(13, 14, 15),
        updated = Seq(16, 17, 18)
      ),
      happy = true,
      investigate = true
    )
  }

  private def networkUpdated(): NetworkChangeInfo = {
    NetworkChangeInfo(
      comment = None, // no need to populate here, comment already in ChangeSetInfo
      key = ChangeKey(
        replicationNumber = 1,
        timestamp = Timestamp(2015, 1, 2),
        changeSetId = 1,
        elementId = 2
      ),
      changeType = ChangeType.Update, // <==
      country = Some(Country.nl),
      networkType = NetworkType.hiking,
      networkId = 2,
      networkName = "network two",
      before = Some(
        MetaData(
          version = 1,
          timestamp = Timestamp(2015, 1, 2),
          changeSetId = 1
        )
      ),
      after = Some(
        MetaData(
          version = 2,
          timestamp = Timestamp(2015, 1, 2),
          changeSetId = 1
        )
      ),
      orphanRoutes = RefChanges(),
      orphanNodes = RefChanges(),
      networkDataUpdated = true,
      networkNodes = RefDiffs(),
      routes = RefDiffs(),
      nodes = IdDiffs(),
      ways = IdDiffs(),
      relations = IdDiffs(),
      happy = true,
      investigate = true
    )
  }

  private def networkDeleted(): NetworkChangeInfo = {
    NetworkChangeInfo(
      comment = None, // no need to populate here, comment already in ChangeSetInfo
      key = ChangeKey(
        replicationNumber = 1,
        timestamp = Timestamp(2015, 1, 2),
        changeSetId = 1,
        elementId = 1
      ),
      changeType = ChangeType.Delete, // <==
      country = Some(Country.nl),
      networkType = NetworkType.hiking,
      networkId = 1,
      networkName = "network one",
      before = Some(
        MetaData(
          version = 1,
          timestamp = Timestamp(2015, 1, 2),
          changeSetId = 1
        )
      ),
      after = None,
      orphanRoutes = RefChanges(),
      orphanNodes = RefChanges(),
      networkDataUpdated = true,
      networkNodes = RefDiffs(),
      routes = RefDiffs(),
      nodes = IdDiffs(),
      ways = IdDiffs(),
      relations = IdDiffs(),
      happy = true,
      investigate = true
    )
  }

  private def routeChanges(): Seq[RouteChangeInfo] = {
    Seq(
      RouteChangeInfo(
        id = routeId1,
        version = 123,
        changeKey = ChangeKey(
          replicationNumber = 1,
          timestamp = Timestamp(2015, 1, 2),
          changeSetId = 1,
          elementId = routeId1
        ),
        comment = None,
        before = Some(
          MetaData(
            version = 122,
            timestamp = Timestamp(2015, 1, 2),
            changeSetId = 111
          )
        ),
        after = Some(
          MetaData(
            version = 123,
            timestamp = Timestamp(2015, 1, 2),
            changeSetId = 1
          )
        ),
        removedWays = Seq(
          // WayInfo()
        ),
        addedWays = Seq(
          // WayInfo()
        ),
        updatedWays = Seq(
          // WayInfo()
        ),

        diffs = RouteDiff(),
        nodes = Seq(
          // RawNode()
        ),
        changeSetInfo = None, // no need to populate, already provided above
        geometryDiff = Some(
          GeometryDiff(
            // common: Seq[PointSegment] = Seq.empty, // blue
            // before: Seq[PointSegment] = Seq.empty, // red
            // after: Seq[PointSegment] = Seq.empty // green
          )
        ),
        bounds = Bounds(),
        happy = true,
        investigate = true
      )
    )
  }

  private def nodeChanges(): Seq[NodeChangeInfo] = {
    Seq(
      NodeChangeInfo(
        id = nodeId1,
        version = Some(3),
        changeKey = ChangeKey(
          replicationNumber = 1,
          timestamp = Timestamp(2015, 1, 2),
          changeSetId = 1,
          elementId = nodeId1
        ),
        changeTags = Tags.from(), // not used
        comment = None,
        before = Some(
          MetaData(
            version = 2,
            timestamp = Timestamp(2015, 1, 2),
            changeSetId = 111
          )
        ),
        after = Some(
          MetaData(
            version = 3,
            timestamp = Timestamp(2015, 1, 2),
            changeSetId = 1
          )
        ),
        connectionChanges = Seq(
          RefBooleanChange(
            ref = Ref(routeId1, "01-02"), // TODO of moet dit een netwerk referentie zijn?
            after = true
          )
        ),
        roleConnectionChanges = Seq(
          RefBooleanChange(
            ref = Ref(routeId1, "01-02"), // TODO of moet dit een netwerk referentie zijn?
            after = true
          )
        ),
        definedInNetworkChanges = Seq(
          RefBooleanChange(
            ref = Ref(1, "network one"),
            after = true
          ),
          RefBooleanChange(
            ref = Ref(2, "network two"),
            after = false
          )
        ),
        tagDiffs = Some(
          TagDiffs(
            //mainTags: Seq[TagDetail] = Seq.empty, // display above separator line
            //extraTags: Seq[TagDetail] = Seq.empty // display below separator line
          )
        ),
        nodeMoved = Some(
          NodeMoved( // TODO take data from actual node
            before = LatLonImpl("0", "0"),
            after = LatLonImpl("0", "0"),
            distance = 10
          )
        ),
        addedToRoute = Seq(
          // Ref()
        ),
        removedFromRoute = Seq(
          // Ref()
        ),
        addedToNetwork = Seq(
          // Ref()
        ),
        removedFromNetwork = Seq(
          // Ref()
        ),
        factDiffs = FactDiffs(
          // resolved: Set[Fact] = Set.empty,
          // introduced: Set[Fact] = Set.empty,
          // remaining: Set[Fact] = Set.empty
        ),
        facts = Seq(
          Fact.WasOrphan
        ),
        happy = true,
        investigate = true
      ),
      NodeChangeInfo(
        id = nodeId2,
        version = Some(3),
        changeKey = ChangeKey(
          replicationNumber = 1,
          timestamp = Timestamp(2015, 1, 2),
          changeSetId = 1,
          elementId = nodeId2
        ),
        changeTags = Tags.from(), // not used
        comment = None,
        before = Some(
          MetaData(
            version = 1,
            timestamp = Timestamp(2015, 1, 2),
            changeSetId = 111
          )
        ),
        after = Some(
          MetaData(
            version = 1,
            timestamp = Timestamp(2015, 1, 2),
            changeSetId = 1
          )
        ),
        connectionChanges = Seq(),
        roleConnectionChanges = Seq(),
        definedInNetworkChanges = Seq(),
        tagDiffs = None,
        nodeMoved = None,
        addedToRoute = Seq(),
        removedFromRoute = Seq(),
        addedToNetwork = Seq(),
        removedFromNetwork = Seq(),
        factDiffs = FactDiffs(),
        facts = Seq(),
        happy = true,
        investigate = true
      ),
      NodeChangeInfo(
        id = nodeId3,
        version = Some(3),
        changeKey = ChangeKey(
          replicationNumber = 1,
          timestamp = Timestamp(2015, 1, 2),
          changeSetId = 1,
          elementId = nodeId3
        ),
        changeTags = Tags.from(), // not used
        comment = None,
        before = Some(
          MetaData(
            version = 5,
            timestamp = Timestamp(2015, 1, 2),
            changeSetId = 111
          )
        ),
        after = Some(
          MetaData(
            version = 6,
            timestamp = Timestamp(2015, 1, 2),
            changeSetId = 1
          )
        ),
        connectionChanges = Seq(),
        roleConnectionChanges = Seq(),
        definedInNetworkChanges = Seq(),
        tagDiffs = None,
        nodeMoved = None,
        addedToRoute = Seq(),
        removedFromRoute = Seq(),
        addedToNetwork = Seq(),
        removedFromNetwork = Seq(),
        factDiffs = FactDiffs(),
        facts = Seq(),
        happy = true,
        investigate = true
      )
    )
  }

}
