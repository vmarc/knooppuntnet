package kpn.server.api.analysis.pages

import kpn.api.common.Bounds
import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.LatLonImpl
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.ChangeSetPage
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.NetworkChangeInfo
import kpn.api.common.changes.details.RefBooleanChange
import kpn.api.common.common.KnownElements
import kpn.api.common.common.Ref
import kpn.api.common.data.MetaData
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.node.NodeMoved
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.node.NodeChangeInfo
import kpn.api.common.route.GeometryDiff
import kpn.api.common.route.RouteChangeInfo
import kpn.api.custom.ChangeType
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

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
      orphanRouteChanges = Seq.empty, // TODO add meaningful values here
      orphanNodeChanges = Seq.empty, // TODO add meaningful values here
      routeChanges = routeChanges(),
      nodeChanges = nodeChanges(),
      knownElements = KnownElements(
        nodeIds = Set(nodeId1, nodeId2), // no link in page for any other node
        routeIds = Set(routeId1, routeId2) // no link in page for any other route
      )
    )
  }

  private def changeSetSummary(): ChangeSetSummary = {
    val key = ChangeKey(
      replicationNumber = 1,
      timestamp = Timestamp(2015, 1, 2),
      changeSetId = 1,
      elementId = 1
    )
    ChangeSetSummary(
      _id = key.toId,
      key = key,
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
      subsetAnalyses = Seq.empty,
      locationChanges = Seq.empty,
      locations = Seq.empty,
      happy = true,
      investigate = true,
      impact = true
    )
  }

  private def changeSetInfo(): ChangeSetInfo = {
    ChangeSetInfo(
      _id = 1,
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
      0,
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
      0,
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
      0,
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
            mainTags = Seq(
              TagDetail(TagDetailType.Update, "key1", Some("before"), Some("after"))
            ),
            extraTags = Seq(
              TagDetail(TagDetailType.Add, "key2", None, Some("after")),
              TagDetail(TagDetailType.Same, "key3", Some("value"), Some("value")),
              TagDetail(TagDetailType.Update, "key4", Some("before"), Some("after")),
              TagDetail(TagDetailType.Delete, "key5", Some("before"), None)
            )
          )
        ),
        nodeMoved = Some(
          NodeMoved(
            before = LatLonImpl("51.46774", "4.46839"),
            after = LatLonImpl("51.467", "4.468"),
            distance = 10
          )
        ),
        addedToRoute = Seq(
          Ref(11, "01-02")
        ),
        removedFromRoute = Seq(
          Ref(12, "02-03")
        ),
        addedToNetwork = Seq(
          Ref(1, "network-1")
        ),
        removedFromNetwork = Seq(
          Ref(1, "network-2")
        ),
        factDiffs = Some(
          FactDiffs(
            resolved = Seq(Fact.NodeInvalidSurveyDate),
            introduced = Seq(Fact.LostBicycleNodeTag),
            remaining = Seq(Fact.LostCanoeNodeTag)
          )
        ),
        facts = Seq.empty,
        initialTags = None,
        initialLatLon = None,
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
        connectionChanges = Seq.empty,
        roleConnectionChanges = Seq.empty,
        definedInNetworkChanges = Seq.empty,
        tagDiffs = None,
        nodeMoved = None,
        addedToRoute = Seq.empty,
        removedFromRoute = Seq.empty,
        addedToNetwork = Seq.empty,
        removedFromNetwork = Seq.empty,
        factDiffs = Some(
          FactDiffs(
            resolved = Seq(Fact.LostCanoeNodeTag),
            introduced = Seq(Fact.LostHorseNodeTag),
            remaining = Seq(Fact.LostBicycleNodeTag)
          )
        ),
        facts = Seq.empty,
        initialTags = None,
        initialLatLon = None,
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
        connectionChanges = Seq.empty,
        roleConnectionChanges = Seq.empty,
        definedInNetworkChanges = Seq.empty,
        tagDiffs = None,
        nodeMoved = None,
        addedToRoute = Seq.empty,
        removedFromRoute = Seq.empty,
        addedToNetwork = Seq.empty,
        removedFromNetwork = Seq.empty,
        factDiffs = None,
        facts = Seq.empty,
        initialTags = Some(
          Tags.from(
            "a" -> "1",
            "b" -> "2",
            "c" -> "3",
          )
        ),
        initialLatLon = Some(
          LatLonImpl(
            latitude = "51.5291600",
            longitude = "4.297800"
          )
        ),
        happy = true,
        investigate = true
      )
    )
  }
}
