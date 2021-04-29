package kpn.server.analyzer.engine.changes.node

import kpn.api.common.LatLon
import kpn.api.common.LatLonImpl
import kpn.api.common.data.Node
import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.TestObjects
import kpn.core.analysis.NetworkNode
import kpn.core.analysis.NetworkNodeInfo
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.location.NodeLocationAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.changes.ElementIds
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.NodeTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.NodeLoader
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.NodeInfoBuilderImpl
import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class UnreferencedNodeProcessorTest extends UnitTest with MockFactory with TestObjects {

  private val latLonBefore = LatLonImpl("51.5291500", "4.297700")
  private val latLonAfter = LatLonImpl("51.5291600", "4.297800")
  private val movedDistance = 7

  // TODO CHANGE Create NetworkDeleteTest integration test cases for all tests in this file
  test("no updates when node is still referenced by other network") {

    val t = new TestSetup()

    val nodeId = 1001L
    val networkId = 11L

    t.analysisContext.data.networks.watched.add(networkId, ElementIds(nodeIds = Set(nodeId)))

    val candidateUnreferencedNodes = Seq(t.networkNodeInfo(nodeId))

    val nodeChanges = t.processor.process(t.context, candidateUnreferencedNodes)
    nodeChanges shouldBe empty
  }

  test("no updates when node is still referenced by orphan route") {

    val t = new TestSetup()

    val nodeId = 1001L
    val routeId = 101L

    t.analysisContext.data.orphanRoutes.watched.add(routeId, ElementIds(nodeIds = Set(nodeId)))

    val candidateUnreferencedNodes = Seq(t.networkNodeInfo(nodeId))

    val nodeChanges = t.processor.process(t.context, candidateUnreferencedNodes)
    nodeChanges shouldBe empty
  }

  test("unreferenced node for which the 'after' cannot be loaded is marked as non-active") {

    val t = new TestSetup()

    val nodeId: Long = 1001L

    pending
    val candidateUnreferencedNodes = Seq(t.networkNodeInfo(nodeId))

    //    (t.nodeLoader.loadNode _).when(t.context.timestampAfter, nodeId).returns(None).once()
    //
    //    val nodeChanges = t.processor.process(t.context, candidateUnreferencedNodes)
    //
    //    unreferencedNodes.oldOrphanNodes shouldBe empty
    //    unreferencedNodes.newOrphanNodes shouldBe empty
    //    unreferencedNodes.oldIgnoredNodes shouldBe empty
    //    unreferencedNodes.newIgnoredNodes shouldBe empty
    //
    //    t.context._oldNodeChanges._oldGet(nodeId) should matchTo(
    //      Some(
    //        NodeChange(
    //          key = t.context.buildChangeKey(nodeId),
    //          changeType = ChangeType.Delete,
    //          subsets = Seq(Subset.nlHiking, Subset.nlBicycle),
    //          name = "01 / 55",
    //          before = Some(
    //            RawNode(
    //              id = nodeId,
    //              latitude = latLonBefore.latitude,
    //              longitude = latLonBefore.longitude,
    //              version = 1,
    //              timestamp = Timestamp(2016, 8, 11),
    //              changeSetId = t.context.changeSet.id,
    //              tags = Tags.from(
    //                "rwn_ref" -> "01",
    //                "rcn_ref" -> "55"
    //              )
    //            )
    //          ),
    //          after = None,
    //          tagDiffs = None,
    //          nodeMoved = None,
    //          addedToNetwork = Seq.empty,
    //          removedFromNetwork = Seq.empty,
    //          factDiffs = FactDiffs(),
    //          facts = Seq(Fact.Deleted),
    //          happy = false,
    //          investigate = true
    //        )
    //      )
    //    )
    //
    //    (t.analysisRepository.saveNode _).verify(
    //      where { nodeInfo: NodeInfo =>
    //        nodeInfo should matchTo(
    //          NodeInfo(
    //            id = nodeId,
    //            active = false,
    //            ignored = false,
    //            orphan = false,
    //            country = Some(Country.nl),
    //            name = "01 / 55",
    //            rcnName = "55",
    //            rwnName = "01",
    //            latitude = latLonBefore.latitude,
    //            longitude = latLonBefore.longitude,
    //            lastUpdated = Timestamp(2016, 8, 11),
    //            lastUpdatedBy = "user",
    //            tags = Tags.from(
    //              "rwn_ref" -> "01",
    //              "rcn_ref" -> "55"
    //            ),
    //            facts = Seq(Fact.Deleted)
    //          )
    //        )
    //        true
    //      }
    //    ).once()
  }

  test("node becomes orphan") {
    val t = new TestSetup()

    val nodeId: Long = 1001L

    val candidateUnreferencedNodes = Seq(t.networkNodeInfo(nodeId, "01", Tags.from("rwn_ref" -> "01")))

    pending

    //    (t.countryAnalyzer.country _).when(*).returns(Some(Country.nl)).once()
    //
    //    (t.nodeLoader.loadNode _).when(t.context.timestampAfter, nodeId).returns(
    //      Some(
    //        Node(t.rawNode(nodeId, Tags.from("rwn_ref" -> "01"), latLonAfter))
    //      )
    //    ).once()
    //
    //    val unreferencedNodes = t.processor.process(t.context, candidateUnreferencedNodes)
    //
    //    unreferencedNodes.newOrphanNodes should equal(Seq(Ref(nodeId, "01")))
    //    unreferencedNodes.newIgnoredNodes shouldBe empty
    //
    //    assert(t.analysisData.orphanNodes.watched.contains(nodeId))
    //
    //    t.context._oldNodeChanges._oldGet(nodeId) should matchTo(
    //      Some(
    //        NodeChange(
    //          key = t.context.buildChangeKey(nodeId),
    //          ChangeType.Update,
    //          subsets = Seq(Subset.nlHiking),
    //          name = "01",
    //          before = Some(
    //            RawNode(
    //              id = nodeId,
    //              latitude = latLonBefore.latitude,
    //              longitude = latLonBefore.longitude,
    //              version = 1,
    //              timestamp = Timestamp(2016, 8, 11),
    //              changeSetId = t.context.changeSet.id,
    //              tags = Tags.from(
    //                "rwn_ref" -> "01"
    //              )
    //            )
    //          ),
    //          after = Some(
    //            RawNode(
    //              id = nodeId,
    //              latitude = latLonAfter.latitude,
    //              longitude = latLonAfter.longitude,
    //              version = 1,
    //              timestamp = Timestamp(2016, 8, 11),
    //              changeSetId = t.context.changeSet.id,
    //              tags = Tags.from(
    //                "rwn_ref" -> "01"
    //              )
    //            )
    //          ),
    //          tagDiffs = None,
    //          nodeMoved = Some(
    //            NodeMoved(
    //              latLonBefore,
    //              latLonAfter,
    //              movedDistance
    //            )
    //          ),
    //          addedToNetwork = Seq.empty,
    //          removedFromNetwork = Seq.empty,
    //          factDiffs = FactDiffs(),
    //          facts = Seq(Fact.BecomeOrphan),
    //          happy = false,
    //          investigate = true
    //        )
    //      )
    //    )
    //
    //    (t.analysisRepository.saveNode _).verify(
    //      where { nodeInfo: NodeInfo =>
    //        nodeInfo should matchTo(
    //          NodeInfo(
    //            id = nodeId,
    //            active = true,
    //            ignored = false,
    //            orphan = true,
    //            country = Some(Country.nl),
    //            name = "01",
    //            rcnName = "",
    //            rwnName = "01",
    //            latitude = latLonAfter.latitude,
    //            longitude = latLonAfter.longitude,
    //            lastUpdated = Timestamp(2016, 8, 11),
    //            lastUpdatedBy = "user",
    //            tags = Tags.from(
    //              "rwn_ref" -> "01"
    //            ),
    //            facts = Seq.empty
    //          )
    //        )
    //        true
    //      }
    //    ).once()
  }

  test("lost bicycle node tag") {
    testNodeTagLost("rcn_ref", Subset.nlBicycle, Fact.LostBicycleNodeTag)
  }

  test("lost hiking node tag") {
    testNodeTagLost("rwn_ref", Subset.nlHiking, Fact.LostHikingNodeTag)
  }

  private def testNodeTagLost(nodeTagKey: String, subset: Subset, expectedFact: Fact): Unit = {

    val t = new TestSetup()

    val nodeId: Long = 1001L

    val candidateUnreferencedNodes = Seq(t.networkNodeInfo(nodeId, "01", Tags.from(nodeTagKey -> "01")))

    pending

    //    (t.countryAnalyzer.country _).when(*).returns(Some(Country.nl)).once()
    //
    //    (t.nodeLoader.loadNode _).when(t.context.timestampAfter, nodeId).returns(
    //      Some(
    //        Node(t.rawNode(nodeId, Tags.from(), latLonAfter))
    //      )
    //    ).once()
    //
    //    val unreferencedNodes = t.processor.process(t.context, candidateUnreferencedNodes)
    //
    //    unreferencedNodes.newOrphanNodes shouldBe empty
    //    unreferencedNodes.newIgnoredNodes shouldBe empty
    //
    //    t.context._oldNodeChanges._oldGet(nodeId) should matchTo(
    //      Some(
    //        NodeChange(
    //          key = t.context.buildChangeKey(nodeId),
    //          ChangeType.Update,
    //          subsets = Seq(subset),
    //          name = "01",
    //          before = Some(
    //            RawNode(
    //              id = nodeId,
    //              latitude = latLonBefore.latitude,
    //              longitude = latLonBefore.longitude,
    //              version = 1,
    //              timestamp = Timestamp(2016, 8, 11),
    //              changeSetId = t.context.changeSet.id,
    //              tags = Tags.from(
    //                nodeTagKey -> "01"
    //              )
    //            )
    //          ),
    //          after = Some(
    //            RawNode(
    //              id = nodeId,
    //              latitude = latLonAfter.latitude,
    //              longitude = latLonAfter.longitude,
    //              version = 1,
    //              timestamp = Timestamp(2016, 8, 11),
    //              changeSetId = t.context.changeSet.id,
    //              tags = Tags.from()
    //            )
    //          ),
    //          tagDiffs = Some(
    //            TagDiffs(
    //              mainTags = Seq(
    //                TagDetail(action = TagDetailType.Delete, nodeTagKey, Some("01"), None)
    //              )
    //            )
    //          ),
    //          nodeMoved = Some(
    //            NodeMoved(
    //              latLonBefore,
    //              latLonAfter,
    //              movedDistance
    //            )
    //          ),
    //          addedToNetwork= Seq.empty,
    //          removedFromNetwork= Seq.empty,
    //          factDiffs = FactDiffs(),
    //          facts = Seq(expectedFact),
    //          happy = false,
    //          investigate = true
    //        )
    //      )
    //    )
    //
    //    (t.analysisRepository.saveNode _).verify(
    //      where { nodeInfo: NodeInfo =>
    //        nodeInfo should matchTo(
    //          NodeInfo(
    //            id = nodeId,
    //            active = false,
    //            ignored = false,
    //            orphan = false,
    //            country = Some(Country.nl),
    //            name = "",
    //            rcnName = "",
    //            rwnName = "",
    //            latitude = latLonAfter.latitude,
    //            longitude = latLonAfter.longitude,
    //            lastUpdated = Timestamp(2016, 8, 11),
    //            lastUpdatedBy = "user",
    //            tags = Tags.from(),
    //            facts = Seq.empty
    //          )
    //        )
    //        true
    //      }
    //    ).once()
  }

  test("lost hiking node tag, but still retain bicyle node tag and become orphan") {

    val t = new TestSetup()

    val nodeId: Long = 1001L

    val candidateUnreferencedNodes = Seq(t.networkNodeInfo(nodeId))

    pending

    //    (t.countryAnalyzer.country _).when(*).returns(Some(Country.nl)).once()
    //
    //    (t.nodeLoader.loadNode _).when(t.context.timestampAfter, nodeId).returns(
    //      Some(
    //        Node(t.rawNode(nodeId, Tags.from("rcn_ref" -> "55"), latLonAfter))
    //      )
    //    ).once()
    //
    //    val unreferencedNodes = t.processor.process(t.context, candidateUnreferencedNodes)
    //
    //    unreferencedNodes.newOrphanNodes should equal(Seq(Ref(nodeId, "55")))
    //    unreferencedNodes.newIgnoredNodes shouldBe empty
    //
    //    t.context._oldNodeChanges._oldGet(nodeId) should matchTo(
    //      Some(
    //        NodeChange(
    //          key = t.context.buildChangeKey(nodeId),
    //          ChangeType.Update,
    //          subsets = Seq(Subset.nlBicycle),
    //          name = "55",
    //          before = Some(
    //            RawNode(
    //              id = nodeId,
    //              latitude = latLonBefore.latitude,
    //              longitude = latLonBefore.longitude,
    //              version = 1,
    //              timestamp = Timestamp(2016, 8, 11),
    //              changeSetId = t.context.changeSet.id,
    //              tags = Tags.from(
    //                "rwn_ref" -> "01",
    //                "rcn_ref" -> "55"
    //              )
    //            )
    //          ),
    //          after = Some(
    //            RawNode(
    //              id = nodeId,
    //              latitude = latLonAfter.latitude,
    //              longitude = latLonAfter.longitude,
    //              version = 1,
    //              timestamp = Timestamp(2016, 8, 11),
    //              changeSetId = t.context.changeSet.id,
    //              tags = Tags.from(
    //                "rcn_ref" -> "55"
    //              )
    //            )
    //          ),
    //          tagDiffs = Some(
    //            TagDiffs(
    //              mainTags = Seq(
    //                TagDetail(action = TagDetailType.Same, "rcn_ref", Some("55"), Some("55")),
    //                TagDetail(action = TagDetailType.Delete, "rwn_ref", Some("01"), None)
    //              )
    //            )
    //          ),
    //          nodeMoved = Some(
    //            NodeMoved(
    //              latLonBefore,
    //              latLonAfter,
    //              movedDistance
    //            )
    //          ),
    //          addedToNetwork= Seq.empty,
    //          removedFromNetwork= Seq.empty,
    //          factDiffs = FactDiffs(),
    //          facts = Seq(Fact.BecomeOrphan, Fact.LostHikingNodeTag),
    //          happy = false,
    //          investigate = true
    //        )
    //      )
    //    )
    //
    //    (t.analysisRepository.saveNode _).verify(
    //      where { nodeInfo: NodeInfo =>
    //        nodeInfo should matchTo(
    //          NodeInfo(
    //            id = nodeId,
    //            active = true,
    //            ignored = false,
    //            orphan = true,
    //            country = Some(Country.nl),
    //            name = "55",
    //            rcnName = "55",
    //            rwnName = "",
    //            latitude = latLonAfter.latitude,
    //            longitude = latLonAfter.longitude,
    //            lastUpdated = Timestamp(2016, 8, 11),
    //            lastUpdatedBy = "user",
    //            tags = Tags.from(
    //              "rcn_ref" -> "55"
    //            ),
    //            facts = Seq.empty
    //          )
    //        )
    //        true
    //      }
    //    ).once()
  }

  test("orphan node looses node tag") {

    val t = new TestSetup()

    val nodeId: Long = 1001L

    t.analysisContext.data.orphanNodes.watched.add(nodeId)

    val candidateUnreferencedNodes = Seq(t.networkNodeInfo(nodeId, "01", Tags.from("rwn_ref" -> "01")))

    (t.countryAnalyzer.country _).when(*).returns(Some(Country.nl))

    pending

    //    (t.nodeLoader.loadNode _).when(t.context.timestampAfter, nodeId).returns(
    //      Some(
    //        Node(t.rawNode(nodeId, Tags.from(), latLonAfter))
    //      )
    //    ).once()
    //
    //    val unreferencedNodes = t.processor.process(t.context, candidateUnreferencedNodes)
    //
    //    unreferencedNodes.newOrphanNodes shouldBe empty
    //    unreferencedNodes.newIgnoredNodes shouldBe empty
    //
    //    unreferencedNodes.oldOrphanNodes should equal(Seq(Ref(nodeId, "01")))
    //    unreferencedNodes.oldIgnoredNodes shouldBe empty
    //
    //    assert(!t.analysisData.orphanNodes.watched.contains(nodeId))
    //
    //    t.context._oldNodeChanges._oldGet(nodeId) should matchTo(
    //      Some(
    //        NodeChange(
    //          key = t.context.buildChangeKey(nodeId),
    //          ChangeType.Update,
    //          subsets = Seq(Subset.nlHiking),
    //          name = "01",
    //          before = Some(
    //            RawNode(
    //              id = nodeId,
    //              latitude = latLonBefore.latitude,
    //              longitude = latLonBefore.longitude,
    //              version = 1,
    //              timestamp = Timestamp(2016, 8, 11),
    //              changeSetId = t.context.changeSet.id,
    //              tags = Tags.from(
    //                "rwn_ref" -> "01"
    //              )
    //            )
    //          ),
    //          after = Some(
    //            RawNode(
    //              id = nodeId,
    //              latitude = latLonAfter.latitude,
    //              longitude = latLonAfter.longitude,
    //              version = 1,
    //              timestamp = Timestamp(2016, 8, 11),
    //              changeSetId = t.context.changeSet.id,
    //              tags = Tags.from()
    //            )
    //          ),
    //          tagDiffs = Some(
    //            TagDiffs(
    //              mainTags = Seq(
    //                TagDetail(action = TagDetailType.Delete, "rwn_ref", Some("01"), None)
    //              )
    //            )
    //          ),
    //          nodeMoved = Some(
    //            NodeMoved(
    //              latLonBefore,
    //              latLonAfter,
    //              movedDistance
    //            )
    //          ),
    //          addedToNetwork= Seq.empty,
    //          removedFromNetwork= Seq.empty,
    //          factDiffs = FactDiffs(),
    //          facts = Seq(Fact.LostHikingNodeTag, Fact.WasOrphan),
    //          happy = false,
    //          investigate = true
    //        )
    //      )
    //    )
    //
    //    (t.analysisRepository.saveNode _).verify(
    //      where { nodeInfo: NodeInfo =>
    //        nodeInfo should matchTo(
    //          NodeInfo(
    //            id = nodeId,
    //            active = false,
    //            ignored = false,
    //            orphan = false,
    //            country = Some(Country.nl),
    //            name = "",
    //            rcnName = "",
    //            rwnName = "",
    //            latitude = latLonAfter.latitude,
    //            longitude = latLonAfter.longitude,
    //            lastUpdated = Timestamp(2016, 8, 11),
    //            lastUpdatedBy = "user",
    //            tags = Tags.from(),
    //            facts = Seq.empty
    //          )
    //        )
    //        true
    //      }
    //    ).once()
  }

  test("ignored node looses node tag") {

    val t = new TestSetup()

    val nodeId: Long = 1001L

    val candidateUnreferencedNodes = Seq(t.networkNodeInfo(nodeId, "01", Tags.from("rwn_ref" -> "01")))

    (t.countryAnalyzer.country _).when(*).returns(Some(Country.nl))

    pending

    //    (t.nodeLoader.loadNodes _).when(t.context.timestampAfter, Seq(nodeId)).returns(
    //      Seq(
    //        Node(t.rawNode(nodeId, Tags.from(), latLonAfter))
    //      )
    //    ).once()
    //
    //    val unreferencedNodes = t.processor.process(t.context, candidateUnreferencedNodes)
    //

    //    unreferencedNodes.newOrphanNodes shouldBe empty
    //    unreferencedNodes.newIgnoredNodes shouldBe empty
    //
    //    unreferencedNodes.oldOrphanNodes shouldBe empty
    //    unreferencedNodes.oldIgnoredNodes should equal(Seq(Ref(nodeId, "01")))
    //
    //    assert(!t.analysisData.orphanNodes.ignored.contains(nodeId))
    //
    //    t.context._oldNodeChanges._oldGet(nodeId) should matchTo(
    //      Some(
    //        NodeChange(
    //          key = t.context.buildChangeKey(nodeId),
    //          ChangeType.Update,
    //          subsets = Seq(Subset.nlHiking),
    //          name = "01",
    //          before = Some(
    //            RawNode(
    //              id = nodeId,
    //              latitude = latLonBefore.latitude,
    //              longitude = latLonBefore.longitude,
    //              version = 1,
    //              timestamp = Timestamp(2016, 8, 11),
    //              changeSetId = t.context.changeSet.id,
    //              tags = Tags.from(
    //                "rwn_ref" -> "01"
    //              )
    //            )
    //          ),
    //          after = Some(
    //            RawNode(
    //              id = nodeId,
    //              latitude = latLonAfter.latitude,
    //              longitude = latLonAfter.longitude,
    //              version = 1,
    //              timestamp = Timestamp(2016, 8, 11),
    //              changeSetId = t.context.changeSet.id,
    //              tags = Tags.from()
    //            )
    //          ),
    //          tagDiffs = Some(
    //            TagDiffs(
    //              mainTags = Seq(
    //                TagDetail(action = TagDetailType.Delete, "rwn_ref", Some("01"), None)
    //              )
    //            )
    //          ),
    //          nodeMoved = Some(
    //            NodeMoved(
    //              latLonBefore,
    //              latLonAfter,
    //              movedDistance
    //            )
    //          ),
    //          addedToNetwork= Seq.empty,
    //          removedFromNetwork= Seq.empty,
    //          factDiffs = FactDiffs(),
    //          facts = Seq(Fact.LostHikingNodeTag, Fact.WasIgnored),
    //          happy = false,
    //          investigate = true
    //        )
    //      )
    //    )
    //
    //    (t.analysisRepository.saveNode _).verify(
    //      where { nodeInfo: NodeInfo =>
    //        nodeInfo should matchTo(
    //          NodeInfo(
    //            id = nodeId,
    //            active = false,
    //            ignored = false,
    //            orphan = false,
    //            country = Some(Country.nl),
    //            name = "",
    //            rcnName = "",
    //            rwnName = "",
    //            latitude = latLonAfter.latitude,
    //            longitude = latLonAfter.longitude,
    //            lastUpdated = Timestamp(2016, 8, 11),
    //            lastUpdatedBy = "user",
    //            tags = Tags.from(),
    //            facts = Seq.empty
    //          )
    //        )
    //        true
    //      }
    //    ).once()
  }


  class TestSetup() {

    val analysisContext = new AnalysisContext()
    val analysisRepository: AnalysisRepository = stub[AnalysisRepository]
    val nodeLoader: NodeLoader = stub[NodeLoader]
    val countryAnalyzer: CountryAnalyzer = stub[CountryAnalyzer]
    val nodeAnalyzer: NodeAnalyzer = new NodeAnalyzerImpl()
    val tileCalculator = new TileCalculatorImpl()
    val nodeTileAnalyzer = new NodeTileAnalyzerImpl(tileCalculator)
    private val nodeLocationAnalyzer = stub[NodeLocationAnalyzer]
    (nodeLocationAnalyzer.locate _).when(*, *).returns(None)
    val nodeInfoBuilder = new NodeInfoBuilderImpl(nodeAnalyzer, nodeTileAnalyzer, nodeLocationAnalyzer)

    val processor = new UnreferencedNodeProcessorImpl(
      analysisContext,
      analysisRepository,
      nodeLoader,
      countryAnalyzer,
      nodeInfoBuilder,
      nodeLocationAnalyzer
    )

    val context: ChangeSetContext = newChangeSetContext()

    def rawNode(nodeId: Long, tags: Tags, position: LatLon): RawNode = {
      RawNode(
        id = nodeId,
        latitude = position.latitude,
        longitude = position.longitude,
        version = 1,
        timestamp = Timestamp(2016, 8, 11),
        changeSetId = context.changeSet.id,
        tags = tags
      )
    }

    def networkNodeInfo(nodeId: Long): NetworkNodeInfo = {
      networkNodeInfo(nodeId, "01 / 55", Tags.from("rwn_ref" -> "01", "rcn_ref" -> "55"))
    }

    def networkNodeInfo(nodeId: Long, name: String, tags: Tags): NetworkNodeInfo = {
      NetworkNodeInfo(
        networkNode = NetworkNode(
          node = Node(rawNode(nodeId, tags = tags, latLonBefore)),
          name = name,
          longName = None,
          country = Some(Country.nl),
          None
        ),
        connection = false,
        roleConnection = false,
        definedInRelation = false,
        definedInRoute = false,
        referencedInRoutes = Seq.empty,
        integrityCheck = None,
        lastSurvey = None,
        facts = Seq.empty
      )
    }
  }
}
