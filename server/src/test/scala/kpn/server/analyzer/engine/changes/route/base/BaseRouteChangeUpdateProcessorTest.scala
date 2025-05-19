package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.Fact
import kpn.api.common.RouteType
import kpn.api.common.route.RouteNodes
import kpn.api.custom.Relation
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.RawRouteDoc
import kpn.core.test.SharedTestObjects
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

class BaseRouteChangeUpdateProcessorTest extends UnitTest with SharedTestObjects {

  private class Setup {
    val log: MockLog = Log.mock
    val analysisContext = new AnalysisContext()
    val routeRepository: RouteRepository = stub[RouteRepository]
    val baseRouteMainAnalyzer: BaseRouteMainAnalyzer = stub[BaseRouteMainAnalyzer]
    val rawDataRepository: RawDataRepository = stub[RawDataRepository]
    val baseRouteDocBuilder: BaseRouteDocBuilder = stub[BaseRouteDocBuilder]
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
    (setup.rawDataRepository.route _).when(*, 11).returns(Some(rawRouteDoc)).once()

    val beforeBaseRouteDoc = newBaseRouteDoc(
      newRouteSummary(11, name = "before"),
      nodes = RouteNodes(
        startNode = Some(newRouteNode(1001, "01")),
        endNode = Some(newRouteNode(1002, "02")),
      )
    )
    (setup.routeRepository.findBaseRouteById _).when(*).returns(Some(beforeBaseRouteDoc)).once()

    val analysisResult = buildAnalysisResult(rawRouteDoc)
    (setup.baseRouteMainAnalyzer.analyze _).when(*, *, *).returns(analysisResult).once()

    val afterBaseRouteDoc = newBaseRouteDoc(newRouteSummary(11, name = "after"))
    (setup.baseRouteDocBuilder.build _).when(*).returns(afterBaseRouteDoc).once()

    // execute
    val updatedChangeSetContext = setup.process()

    // verify
    assertEqual(
      setup.analysisContext.watched.routes.get(11),
      Some(ElementIds(nodeIds = Set(1001, 1002)))
    )

    (setup.routeRepository.saveBaseRoute _).verify(
      where((doc: BaseRouteDoc) => doc._id == 11 && doc.summary.name == "after")
    ).once()

    assertEqual(updatedChangeSetContext.impactedTileIds, Seq("updated-tile"))
    assertEqual(updatedChangeSetContext.impactedNodeIds, Seq(1001, 1002))
    assertEqual(updatedChangeSetContext.impactedRouteIds, Seq(11))
  }

  test("log warning if route not found in overpass database") {

    // setup
    val setup = new Setup()
    (setup.rawDataRepository.route _).when(*, 11).returns(None).once()

    // execute
    val updatedChangeSetContext = setup.process()

    // verify
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
    (setup.rawDataRepository.route _).when(*, 11).returns(Some(rawRouteDoc)).once()
    (setup.baseRouteMainAnalyzer.analyze _).when(*, *, *).returns(buildAbortedAnalysisResult(rawRouteDoc)).once()

    // execute
    val updatedChangeSetContext = setup.process()

    // verify
    setup.log.messages shouldBe empty
    setup.analysisContext.watched.routes shouldBe empty

    updatedChangeSetContext.impactedTileIds shouldBe empty
    updatedChangeSetContext.impactedNodeIds shouldBe empty
    updatedChangeSetContext.impactedRouteIds.shouldEqual(Seq(11))

    (setup.baseRouteDocBuilder.build _).verify(*).never()
    (setup.routeRepository.saveBaseRoute _).verify(*).never()
  }

  test("route analysis is aborted with LostRouteTags") {

    pendingRedesign() // TODO redesign - review LostRouteTags handling

    // setup
    val setup = new Setup()
    val rawRouteDoc = buildRawRouteDoc()
    (setup.rawDataRepository.route _).when(*, 11).returns(Some(rawRouteDoc)).once()
    (setup.baseRouteMainAnalyzer.analyze _).when(*, *, *).returns(buildAnalysisResult(rawRouteDoc).copy(abort = true, facts = Seq(Fact.LostRouteTags))).once()

    // execute
    val updatedChangeSetContext = setup.process()

    // verify
    assertEqual(
      setup.analysisContext.watched.routes.get(11),
      Some(ElementIds(nodeIds = Set(1001, 1002)))
    )

    (setup.routeRepository.saveBaseRoute _).verify(
      where((doc: BaseRouteDoc) => doc._id == 11)
    ).once()

    assertEqual(updatedChangeSetContext.impactedTileIds, Seq("1-1-1-11", "2-2-2-11"))
    assertEqual(updatedChangeSetContext.impactedNodeIds, Seq(1001, 1002))
    assertEqual(updatedChangeSetContext.impactedRouteIds, Seq(11))
  }

  test("route looses required tag(s)") {

    // setup
    val setup = new Setup()

    val rawRouteDoc = buildRawRouteDoc()
    (setup.rawDataRepository.route _).when(*, 11).returns(Some(rawRouteDoc)).once()

    val beforeBaseRouteDoc = newBaseRouteDoc(
      newRouteSummary(11, name = "before"),
      nodes = RouteNodes(
        startNode = Some(newRouteNode(1001, "01")),
        endNode = Some(newRouteNode(1002, "02")),
      )
    )

    val analysisResult = buildAnalysisResult(rawRouteDoc).copy(abort = true, facts = Seq(Fact.RouteTagMissing))
    (setup.baseRouteMainAnalyzer.analyze _).when(*, *, *).returns(analysisResult).once()

    // execute
    val updatedChangeSetContext = setup.process()

    // verify
    setup.analysisContext.watched.routes.contains(11) shouldBe false

    setup.baseRouteDeleter.deletedRouteIds.shouldEqual(Seq(11))

    (setup.routeRepository.saveBaseRoute _).verify(*).never()
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
      elementIds = ElementIds(nodeIds = Set(1001, 1002)),
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
