package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.Fact
import kpn.api.common.Relation
import kpn.api.common.RouteType
import kpn.api.common.route.RouteNodes
import kpn.core.doc.RawRouteDoc
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newChangeSetContext
import kpn.core.test.TestObjects.newRelation
import kpn.core.test.TestObjects.newRouteNode
import kpn.core.test.TestObjects.newRouteNodeAnalysis
import kpn.core.test.TestObjects.newRouteSummary
import kpn.core.test.TestObjects.newRouteTileData
import kpn.core.util.Log
import kpn.core.util.MockLog
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.base.analyzers.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodesAnalysis
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.repository.RawDataRepository
import kpn.server.repository.RouteRepository
import org.scalamock.stubs.Stub
import org.scalamock.stubs.Stubs

class BaseRouteChangeUpdateProcessorTest extends UnitTest with Stubs {

  private class Setup {
    val log: MockLog = Log.mock
    val analysisContext = new AnalysisContext()
    val routeRepository: Stub[RouteRepository] = stub[RouteRepository]
    (routeRepository.saveBaseRoute _).returnsWith(())
    val baseRouteMainAnalyzer: Stub[BaseRouteMainAnalyzer] = stub[BaseRouteMainAnalyzer]
    val rawDataRepository: Stub[RawDataRepository] = stub[RawDataRepository]
    val baseRouteDocBuilder: Stub[BaseRouteDocBuilder] = stub[BaseRouteDocBuilder]
    val baseRouteChangeUpdateWayProcessor: BaseRouteChangeUpdateWayProcessor = (changeSetContext: ChangeSetContext, before: Relation, after: Relation) => {
      changeSetContext
    }
    val routeTileChangeAnalyzer: BaseRouteChangeUpdateTileProcessor = (changeSetContext: ChangeSetContext, _) => {
      changeSetContext.withImpact(tileIds = Seq("updated-tile"))
    }
    val baseRouteDeleter: BaseRouteChangeDeleterMock = new BaseRouteChangeDeleterMock()

    private val processor = new BaseRouteChangeUpdateProcessor(
      analysisContext,
      rawDataRepository,
      routeRepository,
      baseRouteMainAnalyzer,
      baseRouteDocBuilder,
      baseRouteChangeUpdateWayProcessor,
      routeTileChangeAnalyzer,
      baseRouteDeleter
    )

    def process(): ChangeSetContext = {
      processor.loggedProcess(log, newChangeSetContext(), 11)
    }
  }

  test("update route") {

    // setup
    val setup = new Setup()

    val rawRouteDoc = buildRawRouteDoc()
    (setup.rawDataRepository.route _).returnsWith(Some(rawRouteDoc))

    val beforeBaseRouteDoc = newBaseRouteDoc(
      newRouteSummary(11, name = "before"),
      nodes = RouteNodes(
        startNode = Some(newRouteNode(1001, "01")),
        endNode = Some(newRouteNode(1002, "02")),
      )
    )
    (setup.routeRepository.findBaseRouteById _).returnsWith(Some(beforeBaseRouteDoc))

    val analysisResult = buildAnalysisResult(rawRouteDoc)
    (setup.baseRouteMainAnalyzer.analyze _).returnsWith(analysisResult)

    val afterBaseRouteDoc = newBaseRouteDoc(newRouteSummary(11, name = "after"))
    (setup.baseRouteDocBuilder.build _).returnsWith(afterBaseRouteDoc)

    // execute
    val updatedChangeSetContext = setup.process()

    // verify

    (setup.rawDataRepository.route _).times should equal(1)
    (setup.routeRepository.findBaseRouteById _).times should equal(1)
    (setup.baseRouteMainAnalyzer.analyze _).times should equal(1)
    (setup.baseRouteDocBuilder.build _).times should equal(1)

    assertEqual(
      setup.analysisContext.watched.routes.get(11),
      Some(ElementIds.from(nodeIds = Set(1001, 1002)))
    )

    (setup.routeRepository.saveBaseRoute _).calls.map(doc => (doc._id, doc.summary.name)) should equal(Seq((11, "after")))

    assertEqual(updatedChangeSetContext.impactedTileIds, Seq("updated-tile"))
    assertEqual(updatedChangeSetContext.impactedNodeIds, Seq(1001, 1002))
    assertEqual(updatedChangeSetContext.impactedRouteIds, Seq(11))
  }

  test("log warning if route not found in overpass database") {

    // setup
    val setup = new Setup()
    (setup.rawDataRepository.route _).returnsWith(None)

    // execute
    val updatedChangeSetContext = setup.process()

    // verify
    (setup.rawDataRepository.route _).times should equal(1)
    assertEqual(
      setup.log.messages,
      Seq(
        "WARN overpass route 11 not found"
      )
    )

    assertEqual(updatedChangeSetContext.impactedRouteIds, Seq(11))
  }

  test("no data saved when route analysis is aborted, no log message (assume detail already reported)") {

    // setup
    val setup = new Setup()
    val rawRouteDoc = buildRawRouteDoc()
    (setup.rawDataRepository.route _).returnsWith(Some(rawRouteDoc))
    (setup.baseRouteMainAnalyzer.analyze _).returnsWith(buildAbortedAnalysisResult(rawRouteDoc))

    // execute
    val updatedChangeSetContext = setup.process()

    // verify

    (setup.rawDataRepository.route _).times should equal(1)
    (setup.baseRouteMainAnalyzer.analyze _).times should equal(1)

    setup.log.messages shouldBe empty
    assert(setup.analysisContext.watched.routes.isEmpty)

    updatedChangeSetContext.impactedTileIds shouldBe empty
    updatedChangeSetContext.impactedNodeIds shouldBe empty
    updatedChangeSetContext.impactedRouteIds.shouldEqual(Seq(11))

    (setup.baseRouteDocBuilder.build _).times should equal(0)
    (setup.routeRepository.saveBaseRoute _).times should equal(0)
  }

  test("route analysis is aborted with LostRouteTags") {

    pendingRedesign() // TODO redesign - review LostRouteTags handling

    // setup
    val setup = new Setup()
    val rawRouteDoc = buildRawRouteDoc()
    (setup.rawDataRepository.route _).returnsWith(Some(rawRouteDoc))
    (setup.baseRouteMainAnalyzer.analyze _).returnsWith(buildAnalysisResult(rawRouteDoc).copy(abort = true, facts = Seq(Fact.LostRouteTags)))

    // execute
    val updatedChangeSetContext = setup.process()

    // verify

    (setup.rawDataRepository.route _).times should equal(1)
    (setup.baseRouteMainAnalyzer.analyze _).times should equal(1)

    assertEqual(
      setup.analysisContext.watched.routes.get(11),
      Some(ElementIds.from(nodeIds = Set(1001, 1002)))
    )

    (setup.routeRepository.saveBaseRoute _).calls.map(_._id) should equal(Seq(11))

    assertEqual(updatedChangeSetContext.impactedTileIds, Seq("1-1-1-11", "2-2-2-11"))
    assertEqual(updatedChangeSetContext.impactedNodeIds, Seq(1001, 1002))
    assertEqual(updatedChangeSetContext.impactedRouteIds, Seq(11))
  }

  test("route looses required tag(s)") {

    // setup
    val setup = new Setup()

    val rawRouteDoc = buildRawRouteDoc()
    (setup.rawDataRepository.route _).returnsWith(Some(rawRouteDoc))

    val beforeBaseRouteDoc = newBaseRouteDoc(
      newRouteSummary(11, name = "before"),
      nodes = RouteNodes(
        startNode = Some(newRouteNode(1001, "01")),
        endNode = Some(newRouteNode(1002, "02")),
      )
    )

    val analysisResult = buildAnalysisResult(rawRouteDoc).copy(abort = true, facts = Seq(Fact.RouteTagMissing))
    (setup.baseRouteMainAnalyzer.analyze _).returnsWith(analysisResult)

    // execute
    val updatedChangeSetContext = setup.process()

    // verify
    (setup.rawDataRepository.route _).times should equal(1)
    (setup.baseRouteMainAnalyzer.analyze _).times should equal(1)

    setup.analysisContext.watched.routes.contains(11) shouldBe false

    setup.baseRouteDeleter.deletedRouteIds.shouldEqual(Seq(11))

    (setup.routeRepository.saveBaseRoute _).times should equal(0)
  }

  private def buildRawRouteDoc(): RawRouteDoc = {
    val relation = newRelation(id = 11)
    RawRouteDoc(
      11,
      relation,
      None
    )
  }

  private def buildAnalysisResult(rawRouteDoc: RawRouteDoc): BaseRouteAnalysisContext = {
    BaseRouteAnalysisContext(
      rawRouteDoc.relation,
      rawRouteDoc.subRelationTree,
      _routeTypes = Some(
        Seq(RouteType.hiking)
      ),
      _routeNameAnalysis = Some(
        RouteNameAnalysis(name = Some("01-02"))
      ),
      _routeNodesAnalysis = Some(
        RouteNodesAnalysis(
          startNode = Some(newRouteNodeAnalysis(1001, "01")),
          endNode = Some(newRouteNodeAnalysis(1002, "02")),
        )
      ),
      tiles = Seq(
        "1-1-1-11",
        "2-2-2-11",
      ),
      _tileDatas = Some(
        Seq(
          newRouteTileData(1, 1, 1),
          newRouteTileData(2, 2, 2),
        )
      ),
      elementIds = ElementIds.from(nodeIds = Set(1001, 1002)),
    )
  }

  private def buildAbortedAnalysisResult(rawRouteDoc: RawRouteDoc): BaseRouteAnalysisContext = {
    BaseRouteAnalysisContext(
      rawRouteDoc.relation,
      rawRouteDoc.subRelationTree,
      abort = true,
    )
  }
}
