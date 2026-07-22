package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.Country
import kpn.api.common.RouteLocationAnalysis
import kpn.api.custom.Tag
import kpn.api.time.Timestamps
import kpn.core.doc.RawRouteDoc
import kpn.core.test.TestObjects.newChangeSetContext
import kpn.core.util.Log
import kpn.core.util.MockLog
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteLocationAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteTileAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.repository.RawDataRepository
import kpn.server.repository.RouteRepository
import org.scalamock.stubs.Stub
import org.scalamock.stubs.Stubs

class BaseRouteChangeUpdateProcessorTest extends UnitTest with Stubs {

  test("update route") {

    // setup
    val setup = new Setup()

    val relationVersion1 = new RouteTestData("01-02") {
      node(1001, "01")
      node(1002, "02")
      memberWay(101L, "", 1001, 1002)
    }.data.relations(1).copy(version = 1)

    val relationVersion2 = new RouteTestData("01-03") {
      node(1001, "01")
      node(1003, "03")
      memberWay(101L, "", 1001, 1003)
    }.data.relations(1).copy(version = 2)

    (setup.rawDataRepository.route _).returns {
      case (Timestamps.before, 1) =>
        Some(
          RawRouteDoc(
            1,
            relationVersion1,
            None
          )
        )

      case (Timestamps.after, 1) =>
        Some(
          RawRouteDoc(
            1,
            relationVersion2,
            None
          )
        )
      case _ => None
    }

    // execute
    val updatedChangeSetContext = setup.process()

    // verify

    (setup.rawDataRepository.route _).times should equal(2)

    assertEqual(
      setup.analysisContext.watched.routes.get(1).get,
      ElementIds.from(nodeIds = Set(1001, 1003), wayIds = Set(101))
    )

    (setup.routeRepository.saveBaseRoute _).calls.map(doc => (doc._id, doc.base.name)) should equal(Seq((1, "01-03")))

    assertEqual(updatedChangeSetContext.impactedTileIds, Seq("updated-tile"))
    assertEqual(updatedChangeSetContext.impactedNodeIds, Seq(1002, 1003))
    assertEqual(updatedChangeSetContext.impactedRouteIds, Seq(1))
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
        "WARN overpass route 1 not found"
      )
    )

    assertEqual(updatedChangeSetContext.impactedRouteIds, Seq(1))
  }

  test("no data saved when route analysis is aborted, no log message (assume detail already reported)") {

    // setup
    val setup = new Setup()

    val relation = new RouteTestData("01-02") {
      node(1001, "01")
      node(1002, "02")
      memberWay(101L, "", 1001, 1002)
    }.data.relations(1)
    val relationVersion1 = relation
    val relationVersion2 = relation.copy(version = 1, tags = relation.tags.map(t => if (t.key == "route") Tag("route", "bla") else t))

    (setup.rawDataRepository.route _).returns {
      case (Timestamps.before, 1) =>
        Some(
          RawRouteDoc(
            1,
            relationVersion1,
            None
          )
        )

      case (Timestamps.after, 1) =>
        Some(
          RawRouteDoc(
            1,
            relationVersion2,
            None
          )
        )
      case _ => None
    }

    // execute
    val updatedChangeSetContext = setup.process()

    // verify

    (setup.rawDataRepository.route _).times should equal(1)

    setup.log.messages shouldBe empty
    assert(setup.analysisContext.watched.routes.isEmpty)

    updatedChangeSetContext.impactedTileIds shouldBe empty
    updatedChangeSetContext.impactedNodeIds shouldBe empty
    updatedChangeSetContext.impactedRouteIds.shouldEqual(Seq(1))

    (setup.routeRepository.saveBaseRoute _).times should equal(0)
  }

  //  test("route analysis is aborted with LostRouteTags") {
  //
  //    pendingRedesign() // TODO redesign - review LostRouteTags handling
  //
  //    // setup
  //    val setup = new Setup()
  //    val rawRouteDoc = buildRawRouteDoc()
  //    (setup.rawDataRepository.route _).returnsWith(Some(rawRouteDoc))
  //    (setup.baseRouteMainAnalyzer.analyze _).returnsWith(buildAnalysisResult(rawRouteDoc).copy(abort = true, facts = Seq(Fact.LostRouteTags)))
  //
  //    // execute
  //    val updatedChangeSetContext = setup.process()
  //
  //    // verify
  //
  //    (setup.rawDataRepository.route _).times should equal(1)
  //    (setup.baseRouteMainAnalyzer.analyze _).times should equal(1)
  //
  //    assertEqual(
  //      setup.analysisContext.watched.routes.get(11),
  //      Some(ElementIds.from(nodeIds = Set(1001, 1002)))
  //    )
  //
  //    (setup.routeRepository.saveBaseRoute _).calls.map(_._id) should equal(Seq(11))
  //
  //    assertEqual(updatedChangeSetContext.impactedTileIds, Seq("1-1-1-11", "2-2-2-11"))
  //    assertEqual(updatedChangeSetContext.impactedNodeIds, Seq(1001, 1002))
  //    assertEqual(updatedChangeSetContext.impactedRouteIds, Seq(11))
  //  }

  //  test("route looses required tag(s)") {
  //
  //    // setup
  //    val setup = new Setup()
  //
  //    val rawRouteDoc = buildRawRouteDoc()
  //    (setup.rawDataRepository.route _).returnsWith(Some(rawRouteDoc))
  //
  //    val beforeBaseRouteDoc = newBaseRouteDoc(
  //      11,
  //      base = newRouteBaseData(
  //        name = "before",
  //        nodes = RouteNodes(
  //          startNode = Some(newRouteNode(1001, "01")),
  //          endNode = Some(newRouteNode(1002, "02")),
  //        )
  //      )
  //    )
  //
  //    val analysisResult = buildAnalysisResult(rawRouteDoc).copy(abort = true, facts = Seq(Fact.RouteTagMissing))
  //    (setup.baseRouteMainAnalyzer.analyze _).returnsWith(analysisResult)
  //
  //    // execute
  //    val updatedChangeSetContext = setup.process()
  //
  //    // verify
  //    (setup.rawDataRepository.route _).times should equal(1)
  //    (setup.baseRouteMainAnalyzer.analyze _).times should equal(1)
  //
  //    setup.analysisContext.watched.routes.contains(11) shouldBe false
  //
  //    setup.baseRouteDeleter.deletedRouteIds.shouldEqual(Seq(11))
  //
  //    (setup.routeRepository.saveBaseRoute _).times should equal(0)
  //  }

  //  private def buildAnalysisResult(rawRouteDoc: RawRouteDoc): BaseRouteAnalysisContext = {
  //    BaseRouteAnalysisContext(
  //      rawRouteDoc.relation,
  //      rawRouteDoc.subRelationTree,
  //      _routeTypes = Some(
  //        Seq(RouteType.hiking)
  //      ),
  //      _routeNameAnalysis = Some(
  //        RouteNameAnalysis(name = Some("01-02"))
  //      ),
  //      _routeNodesAnalysis = Some(
  //        RouteNodesAnalysis(
  //          startNode = Some(newRouteNodeAnalysis(1001, "01")),
  //          endNode = Some(newRouteNodeAnalysis(1002, "02")),
  //        )
  //      ),
  //      tiles = Seq(
  //        "1-1-1-11",
  //        "2-2-2-11",
  //      ),
  //      _tileDatas = Some(
  //        Seq(
  //          newRouteTileData(1, 1, 1),
  //          newRouteTileData(2, 2, 2),
  //        )
  //      ),
  //      elementIds = ElementIds.from(nodeIds = Set(1001, 1002)),
  //    )
  //  }

  //  private def buildAbortedAnalysisResult(rawRouteDoc: RawRouteDoc): BaseRouteAnalysisContext = {
  //    BaseRouteAnalysisContext(
  //      rawRouteDoc.relation,
  //      rawRouteDoc.subRelationTree,
  //      abort = true,
  //    )
  //  }

  private class Setup {

    val log: MockLog = Log.mock

    val analysisContext = new AnalysisContext()

    val routeRepository: Stub[RouteRepository] = stub[RouteRepository]
    (routeRepository.saveBaseRoute _).returnsWith(())

    val countryAnalyzer: Stub[BaseRouteCountryAnalyzer] = stub[BaseRouteCountryAnalyzer]
    (countryAnalyzer.analyze _).returns {
      context: BaseRouteAnalysisContext => context.copy(_countries = Some(Seq(Country.nl)))
    }

    val locationAnalyzer: Stub[BaseRouteLocationAnalyzer] = stub[BaseRouteLocationAnalyzer]
    (locationAnalyzer.analyze _).returns {
      context: BaseRouteAnalysisContext =>
        context.copy(_locationAnalysis = Some(RouteLocationAnalysis(None, Seq.empty, Seq.empty)))
    }

    val tileAnalyzer: Stub[BaseRouteTileAnalyzer] = stub[BaseRouteTileAnalyzer]
    (tileAnalyzer.analyze _).returns {
      context: BaseRouteAnalysisContext => context
    }

    val baseRouteMainAnalyzer = new BaseRouteMainAnalyzer(
      countryAnalyzer,
      locationAnalyzer,
      tileAnalyzer
    )
    val rawDataRepository: Stub[RawDataRepository] = stub[RawDataRepository]
    val baseRouteDocBuilder = new BaseRouteDocBuilder()
    val baseRouteDiffWaysAnalyzer: Stub[BaseRouteDiffWaysAnalyzer] = stub[BaseRouteDiffWaysAnalyzer]
    (baseRouteDiffWaysAnalyzer.analyze _).returns {
      case (before: BaseRouteAnalysisContext, after: BaseRouteAnalysisContext) => None
    }

    val routeTileChangeAnalyzer: Stub[BaseRouteChangeUpdateTileProcessor] = stub[BaseRouteChangeUpdateTileProcessor]
    (routeTileChangeAnalyzer.process _).returns {
      case (changeSetContext: ChangeSetContext, context: BaseRouteAnalysisContext) =>
        changeSetContext.withImpact(tileIds = Seq("updated-tile"))
    }

    val baseRouteDeleter: BaseRouteChangeDeleterMock = new BaseRouteChangeDeleterMock()

    private val baseRouteDiffAnalyzer = {
      val baseRouteDiffNameAnalyzer = new BaseRouteDiffNameAnalyzer()
      val baseRouteDiffFactsAnalyzer = new BaseRouteDiffFactsAnalyzer()
      val baseRouteDiffNodesAnalyzer = new BaseRouteDiffNodesAnalyzer()
      val baseRouteDiffMemberAnalyzer = new BaseRouteDiffMemberAnalyzer()
      val babseRouteDiffGeometryAnalyzer = new BaseRouteDiffGeometryAnalyzer()
      new BaseRouteDiffAnalyzer(
        baseRouteDiffNameAnalyzer,
        baseRouteDiffFactsAnalyzer,
        baseRouteDiffNodesAnalyzer,
        baseRouteDiffMemberAnalyzer,
        babseRouteDiffGeometryAnalyzer,
        baseRouteDiffWaysAnalyzer
      )
    }

    private val processor = new BaseRouteChangeUpdateProcessor(
      analysisContext,
      rawDataRepository,
      routeRepository,
      baseRouteMainAnalyzer,
      baseRouteDocBuilder,
      baseRouteDiffAnalyzer,
      routeTileChangeAnalyzer,
      baseRouteDeleter,
      log
    )

    def process(): ChangeSetContext = {
      processor.process(newChangeSetContext(), 1)
    }
  }
}
