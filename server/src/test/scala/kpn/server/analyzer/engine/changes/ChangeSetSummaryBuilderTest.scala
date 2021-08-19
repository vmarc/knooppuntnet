package kpn.server.analyzer.engine.changes

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.LocationChangesTree
import kpn.api.common.LocationChangesTreeNode
import kpn.api.common.NetworkChanges
import kpn.api.common.ReplicationId
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.location.Location
import kpn.api.common.location.LocationCandidate
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.TestObjects
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.changes.node.NodeChangeStateAnalyzer
import kpn.server.analyzer.engine.changes.route.RouteChangeStateAnalyzer
import kpn.server.analyzer.engine.context.ElementIds

class ChangeSetSummaryBuilderTest extends UnitTest with TestObjects {

  test("node and routes removed/added/updated") {

    val changes = ChangeSetChanges(
      routeChanges = Seq(
        RouteChangeStateAnalyzer.analyzed(
          newRouteChange(
            key = newChangeKey(elementId = 11),
            changeType = ChangeType.Delete,
            name = "01-02",
            before = Some(
              newRouteData(country = Some(Country.nl), networkType = NetworkType.hiking)
            ),
            locationAnalysis = newRouteLocationAnalysis(
              candidates = Seq(
                LocationCandidate(
                  location = Location(Seq("nl", "North Brabant", "Roosendaal")),
                  percentage = 100
                )
              )
            )
          )
        ),
        RouteChangeStateAnalyzer.analyzed(
          newRouteChange(
            key = newChangeKey(elementId = 12),
            changeType = ChangeType.Create,
            name = "02-03",
            after = Some(
              newRouteData(country = Some(Country.nl), networkType = NetworkType.hiking)
            ),
            locationAnalysis = newRouteLocationAnalysis(
              candidates = Seq(
                LocationCandidate(
                  location = Location(Seq("nl", "North Brabant", "Roosendaal")),
                  percentage = 100
                )
              )
            )
          )
        ),
        RouteChangeStateAnalyzer.analyzed(
          newRouteChange(
            key = newChangeKey(elementId = 13),
            changeType = ChangeType.Update,
            name = "03-04",
            before = Some(
              newRouteData(country = Some(Country.nl), networkType = NetworkType.hiking)
            ),
            after = Some(
              newRouteData(country = Some(Country.nl), networkType = NetworkType.hiking)
            ),
            locationAnalysis = newRouteLocationAnalysis(
              candidates = Seq(
                LocationCandidate(
                  location = Location(Seq("nl", "North Brabant", "Roosendaal")),
                  percentage = 100
                )
              )
            )
          )
        )
      ),
      nodeChanges = Seq(
        NodeChangeStateAnalyzer.analyzed(
          newNodeChange(
            key = newChangeKey(elementId = 1001),
            changeType = ChangeType.Delete,
            subsets = Seq(Subset.nlHiking),
            locations = Seq("nl", "North Brabant", "Roosendaal"),
            name = "01"
          )
        ),
        NodeChangeStateAnalyzer.analyzed(
          newNodeChange(
            key = newChangeKey(elementId = 1002),
            changeType = ChangeType.Create,
            subsets = Seq(Subset.nlHiking),
            locations = Seq("nl", "North Brabant", "Roosendaal"),
            name = "02"
          )
        ),
        NodeChangeStateAnalyzer.analyzed(
          newNodeChange(
            key = newChangeKey(elementId = 1003),
            changeType = ChangeType.Update,
            subsets = Seq(Subset.nlHiking),
            locations = Seq("nl", "North Brabant", "Roosendaal"),
            name = "03"
          )
        )
      )
    )

    val context = ChangeSetContext(
      ReplicationId(0, 0, 1),
      newChangeSet(),
      ElementIds(),
      changes
    )

    val changeSetSummary = new ChangeSetSummaryBuilder().build(context)

    changeSetSummary should matchTo(
      ChangeSetSummary(
        _id = newChangeKey().toShortId,
        key = newChangeKey(),
        subsets = Seq(Subset.nlHiking),
        timestampFrom = Timestamp(2015, 8, 11, 0, 0, 2),
        timestampUntil = Timestamp(2015, 8, 11, 0, 0, 3),
        networkChanges = NetworkChanges(),
        routeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              removed = Seq(
                newChangeSetElementRef(11, "01-02", investigate = true)
              ),
              added = Seq(
                newChangeSetElementRef(12, "02-03", happy = true)
              ),
              updated = Seq(
                newChangeSetElementRef(13, "03-04")
              )
            )
          )
        ),
        nodeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              removed = Seq(
                newChangeSetElementRef(1001, "01", investigate = true)
              ),
              added = Seq(
                newChangeSetElementRef(1002, "02", happy = true)
              ),
              updated = Seq(
                newChangeSetElementRef(1003, "03")
              )
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(
            Subset.nlHiking,
            happy = true,
            investigate = true
          )
        ),
        trees = Seq(
          LocationChangesTree(
            networkType = NetworkType.hiking,
            locationName = "nl",
            happy = true,
            investigate = true,
            children = Seq(
              LocationChangesTreeNode(
                locationName = "North Brabant",
                routeChanges = ChangeSetElementRefs(), // empty because non-leaf node
                nodeChanges = ChangeSetElementRefs(), // empty because non-leaf node
                children = Seq(
                  LocationChangesTreeNode(
                    locationName = "Roosendaal",
                    routeChanges = ChangeSetElementRefs(
                      removed = Seq(
                        ChangeSetElementRef(
                          id = 11,
                          name = "01-02",
                          happy = false,
                          investigate = true
                        )
                      ),
                      added = Seq(
                        ChangeSetElementRef(
                          id = 12,
                          name = "02-03",
                          happy = true,
                          investigate = false
                        )
                      ),
                      updated = Seq(
                        ChangeSetElementRef(
                          id = 13,
                          name = "03-04",
                          happy = false,
                          investigate = false
                        )
                      )
                    ),
                    nodeChanges = ChangeSetElementRefs(
                      removed = Seq(
                        ChangeSetElementRef(
                          id = 1001,
                          name = "01",
                          happy = false,
                          investigate = true
                        )
                      ),
                      added = Seq(
                        ChangeSetElementRef(
                          id = 1002,
                          name = "02",
                          happy = true,
                          investigate = false
                        )
                      ),
                      updated = Seq(
                        ChangeSetElementRef(
                          id = 1003,
                          name = "03",
                          happy = false,
                          investigate = false
                        )
                      )
                    ),
                    children = Seq.empty, // empty for leaf node
                    happy = true,
                    investigate = true
                  )
                ),
                happy = true,
                investigate = true
              )
            )
          )
        ),
        happy = true,
        investigate = true,
        impact = true
      )
    )
  }

  test("nested locations") {

    val changes = ChangeSetChanges(
      nodeChanges = Seq(
        NodeChangeStateAnalyzer.analyzed(
          newNodeChange(
            key = newChangeKey(elementId = 1001),
            changeType = ChangeType.Update,
            subsets = Seq(Subset.nlHiking),
            locations = Seq("nl", "North Brabant", "Roosendaal"),
            name = "01"
          )
        ),
        NodeChangeStateAnalyzer.analyzed(
          newNodeChange(
            key = newChangeKey(elementId = 1002),
            changeType = ChangeType.Update,
            subsets = Seq(Subset.nlHiking),
            locations = Seq("nl", "North Brabant", "Roosendaal"),
            name = "02"
          )
        ),
        NodeChangeStateAnalyzer.analyzed(
          newNodeChange(
            key = newChangeKey(elementId = 1003),
            changeType = ChangeType.Update,
            subsets = Seq(Subset.beHiking),
            locations = Seq("be", "Antwerp province", "Antwerp arrondissement", "Essen BE"),
            name = "03"
          )
        ),
        NodeChangeStateAnalyzer.analyzed(
          newNodeChange(
            key = newChangeKey(elementId = 1004),
            changeType = ChangeType.Update,
            subsets = Seq(Subset.beBicycle),
            locations = Seq("be", "Antwerp province", "Antwerp arrondissement", "Essen BE"),
            name = "04"
          )
        )
      )
    )

    val context = ChangeSetContext(
      ReplicationId(0, 0, 1),
      newChangeSet(),
      ElementIds(),
      changes
    )

    val changeSetSummary = new ChangeSetSummaryBuilder().build(context)

    changeSetSummary should matchTo(
      ChangeSetSummary(
        _id = newChangeKey().toShortId,
        key = newChangeKey(),
        subsets = Seq(
          Subset.beBicycle,
          Subset.beHiking,
          Subset.nlHiking
        ),
        timestampFrom = Timestamp(2015, 8, 11, 0, 0, 2),
        timestampUntil = Timestamp(2015, 8, 11, 0, 0, 3),
        networkChanges = NetworkChanges(),
        routeChanges = Seq.empty,
        nodeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.beBicycle,
            ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1004, "04")
              )
            )
          ),
          ChangeSetSubsetElementRefs(
            Subset.beHiking,
            ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1003, "03")
              )
            )
          ),
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1001, "01"),
                newChangeSetElementRef(1002, "02"),
              )
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(Subset.beBicycle),
          ChangeSetSubsetAnalysis(Subset.beHiking),
          ChangeSetSubsetAnalysis(Subset.nlHiking)
        ),
        trees = Seq(
          LocationChangesTree(
            networkType = NetworkType.hiking,
            locationName = "be",
            happy = false,
            investigate = false,
            children = Seq(
              LocationChangesTreeNode(
                locationName = "Antwerp province",
                routeChanges = ChangeSetElementRefs(), // empty because non-leaf node
                nodeChanges = ChangeSetElementRefs(), // empty because non-leaf node
                children = Seq(
                  LocationChangesTreeNode(
                    locationName = "Antwerp arrondissement",
                    routeChanges = ChangeSetElementRefs(), // empty because non-leaf node
                    nodeChanges = ChangeSetElementRefs(), // empty because non-leaf node
                    children = Seq(
                      LocationChangesTreeNode(
                        locationName = "Essen BE",
                        routeChanges = ChangeSetElementRefs(),
                        nodeChanges = ChangeSetElementRefs(
                          updated = Seq(
                            ChangeSetElementRef(
                              id = 1003,
                              name = "03",
                              happy = false,
                              investigate = false
                            ),
                          )
                        ),
                        children = Seq.empty, // empty for leaf node
                        happy = false,
                        investigate = false
                      )
                    ),
                    happy = false,
                    investigate = false
                  )
                ),
                happy = false,
                investigate = false
              )
            )
          ),
          LocationChangesTree(
            networkType = NetworkType.hiking,
            locationName = "nl",
            happy = false,
            investigate = false,
            children = Seq(
              LocationChangesTreeNode(
                locationName = "North Brabant",
                routeChanges = ChangeSetElementRefs(), // empty because non-leaf node
                nodeChanges = ChangeSetElementRefs(), // empty because non-leaf node
                children = Seq(
                  LocationChangesTreeNode(
                    locationName = "Roosendaal",
                    routeChanges = ChangeSetElementRefs(),
                    nodeChanges = ChangeSetElementRefs(
                      updated = Seq(
                        ChangeSetElementRef(
                          id = 1001,
                          name = "01",
                          happy = false,
                          investigate = false
                        ),
                        ChangeSetElementRef(
                          id = 1002,
                          name = "02",
                          happy = false,
                          investigate = false
                        )
                      )
                    ),
                    children = Seq.empty, // empty for leaf node
                    happy = false,
                    investigate = false
                  )
                ),
                happy = false,
                investigate = false
              )
            )
          ),
          LocationChangesTree(
            networkType = NetworkType.cycling,
            locationName = "be",
            happy = false,
            investigate = false,
            children = Seq(
              LocationChangesTreeNode(
                locationName = "Antwerp province",
                routeChanges = ChangeSetElementRefs(), // empty because non-leaf node
                nodeChanges = ChangeSetElementRefs(), // empty because non-leaf node
                children = Seq(
                  LocationChangesTreeNode(
                    locationName = "Antwerp arrondissement",
                    routeChanges = ChangeSetElementRefs(), // empty because non-leaf node
                    nodeChanges = ChangeSetElementRefs(), // empty because non-leaf node
                    children = Seq(
                      LocationChangesTreeNode(
                        locationName = "Essen BE",
                        routeChanges = ChangeSetElementRefs(),
                        nodeChanges = ChangeSetElementRefs(
                          updated = Seq(
                            ChangeSetElementRef(
                              id = 1004,
                              name = "04",
                              happy = false,
                              investigate = false
                            ),
                          )
                        ),
                        children = Seq.empty, // empty for leaf node
                        happy = false,
                        investigate = false
                      )
                    ),
                    happy = false,
                    investigate = false
                  )
                ),
                happy = false,
                investigate = false
              )
            )
          )
        ),
        happy = false,
        investigate = false,
        impact = false
      )
    )
  }
}
