package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.RouteType
import kpn.core.test.TestObjects.newChangeSetContext
import kpn.core.test.TestObjects.newRelation
import kpn.core.test.TestObjects.newRouteTileData
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.base.analyzers.RouteNameAnalysis
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.repository.RouteRepository
import org.scalamock.stubs.Stub
import org.scalamock.stubs.Stubs

class BaseRouteChangeUpdateTileProcessorTest extends UnitTest with Stubs {

  test("process tile updates") {
    // setup
    val setup = new Setup()

    // execute
    val changeSetContext = setup.process()

    // verify
    setup.assertTileDeleted("1-1-1-11")
    (setup.routeRepository.saveRouteTile _).calls.map(_._id) should equal(Seq("1-1-3-11", "1-1-4-11"))
    changeSetContext.impactedTileIds should equal(
      Seq(
        "1-1-1-11",
        "1-1-3-11",
        "1-1-4-11"
      )
    )
  }

  private class Setup {
    val routeRepository: Stub[RouteRepository] = stub[RouteRepository]
    (routeRepository.saveRouteTile _).returnsWith(())
    (routeRepository.deleteRouteTile _).returnsWith(())
    private val processor = new BaseRouteChangeUpdateTileProcessorImpl(routeRepository)

    private val context = BaseRouteAnalysisContext(
      relation = newRelation(id = 11),
      subRelationTree = None,
      _routeTypes = Some(Seq(RouteType.hiking)),
      _routeNameAnalysis = Some(RouteNameAnalysis(name = Some("01-02"))),
    )

    private val beforeContext = context.copy(
      _tileDatas = Some(
        Seq(
          newRouteTileData(1, 1, 1), // to be deleted
          newRouteTileData(1, 1, 2), // unchanged
          newRouteTileData(1, 1, 3), // changed
        )
      )
    )

    private val afterContext = context.copy(
      _tileDatas = Some(
        Seq(
          newRouteTileData(1, 1, 2), // unchanged
          newRouteTileData(1, 1, 3, error = Some("error")), // changed
          newRouteTileData(1, 1, 4), // new
        )
      )
    )

    private val beforeRouteTileInfos = RouteTileInfoBuilder.build(beforeContext)
    (routeRepository.routeTiles _).returnsWith(beforeRouteTileInfos)

    def process(): ChangeSetContext = {
      processor.process(newChangeSetContext(), afterContext)
    }

    def assertTileDeleted(tileId: String): Unit = {
      (routeRepository.deleteRouteTile _).calls should equal(Seq(tileId))
    }
  }
}
