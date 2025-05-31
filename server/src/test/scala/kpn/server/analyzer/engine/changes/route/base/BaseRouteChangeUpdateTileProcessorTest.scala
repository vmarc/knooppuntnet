package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.RouteType
import kpn.core.test.SharedTestObjects
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.base.analyzers.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileInfo
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.repository.RouteRepository
import org.scalamock.handlers.CallHandler1

class BaseRouteChangeUpdateTileProcessorTest extends UnitTest with SharedTestObjects {

  test("process tile updates") {
    // setup
    val setup = new Setup()

    // execute
    val changeSetContext = setup.process()

    // verify
    setup.assertTileDeleted("1-1-1-11")
    setup.verifyTileSaved("1-1-2-11").never()
    setup.verifyTileSaved("1-1-3-11").once()
    setup.verifyTileSaved("1-1-4-11").once()

    changeSetContext.impactedTileIds should equal(
      Seq(
        "1-1-1-11",
        "1-1-3-11",
        "1-1-4-11"
      )
    )
  }

  private class Setup {
    private val routeRepository: RouteRepository = stub[RouteRepository]
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
    (routeRepository.routeTiles _).when(*).returns(beforeRouteTileInfos)

    def process(): ChangeSetContext = {
      processor.process(newChangeSetContext(), afterContext)
    }

    def assertTileDeleted(tileId: String): Unit = {
      (routeRepository.deleteRouteTile _).verify(tileId).once()
    }

    def verifyTileSaved(tileId: String): CallHandler1[RouteTileInfo, Unit] = {
      (routeRepository.saveRouteTile _).verify(
        where { (routeTileInfo: RouteTileInfo) =>
          routeTileInfo._id == tileId
        }
      )
    }
  }
}
