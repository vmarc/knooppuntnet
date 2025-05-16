package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.RouteType
import kpn.api.common.SharedTestObjects
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.base.analyzers.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
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
      hierarchy = None,
      _routeTypes = Some(Seq(RouteType.hiking)),
      _routeNameAnalysis = Some(RouteNameAnalysis(name = Some("01-02"))),
    )

    private val beforeContext = context.copy(
      _tileDatas = Some(
        Seq(
          newRouteTileData(1, 1, 1, "to-be-deleted"),
          newRouteTileData(1, 1, 2, "common-unchanged"),
          newRouteTileData(1, 1, 3, "common-changed"),
        )
      )
    )

    private val afterContext = context.copy(
      _tileDatas = Some(
        Seq(
          newRouteTileData(1, 1, 2, "common-unchanged"),
          newRouteTileData(1, 1, 3, "common-changed", error = Some("error")),
          newRouteTileData(1, 1, 4, "new"),
        )
      )
    )

    private val beforeTileDocs = RouteTileDocBuilder.build(beforeContext)
    (routeRepository.routeTiles _).when(*).returns(beforeTileDocs)

    def process(): ChangeSetContext = {
      processor.process(newChangeSetContext(), afterContext)
    }

    def assertTileDeleted(tileId: String): Unit = {
      (routeRepository.deleteRouteTile _).verify(tileId).once()
    }

    def verifyTileSaved(tileId: String): CallHandler1[RouteTileDoc, Unit] = {
      (routeRepository.saveRouteTile _).verify(
        where { (doc: RouteTileDoc) =>
          doc._id == tileId
        }
      )
    }
  }
}
