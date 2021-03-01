package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.ScopedNetworkType
import kpn.core.data.Data
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteNode
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeInfo
import kpn.server.analyzer.engine.analysis.route.segment.Fragment
import kpn.server.analyzer.engine.analysis.route.segment.Path
import kpn.server.analyzer.engine.analysis.route.segment.Segment
import kpn.server.analyzer.engine.analysis.route.segment.SegmentFragment
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedRoute

class SplitNodeRouteAnalyzerTest extends UnitTest {

  test("split node route") {

    val d = new RouteTestData("01-01") {
      node(1, "01")
      node(2, "01")
      node(3, "01")
      memberWay(4, "", 1, 2, 3)
    }.data

    val routeNodeInfos = Map(
      1L -> RouteNodeInfo("01"),
      2L -> RouteNodeInfo("01"),
      3L -> RouteNodeInfo("01")
    )

    val context = analyze(d, routeNodeInfos)
    val splitNodePaths = context.structure.get.splitNodePaths

    val expectedRouteNode1 = RouteNode(
      nodeType = null,
      node = d.nodes(1),
      name = "01",
      alternateName = "01.b",
      definedInWay = true
    )

    val expectedRouteNode2 = RouteNode(
      nodeType = null,
      node = d.nodes(2),
      name = "01",
      alternateName = "01.a",
      definedInWay = true
    )

    val expectedRouteNode3 = RouteNode(
      nodeType = null,
      node = d.nodes(3),
      name = "01",
      alternateName = "01",
      definedInWay = true
    )

    val splitNodePathsWithoutFragmentIds = splitNodePaths.map { path =>
      path.copy(
        segments = path.segments.map { segment =>
          segment.copy(
            fragments = segment.fragments.map { segmentFragment =>
              segmentFragment.copy(
                fragment = segmentFragment.fragment.copy(id = -1)
              )
            }
          )
        }
      )
    }

    splitNodePathsWithoutFragmentIds should equal(
      Seq(
        Path(
          start = Some(expectedRouteNode1),
          end = Some(expectedRouteNode2),
          startNodeId = 1,
          endNodeId = 2,
          segments = Seq(
            Segment(
              "paved",
              Seq(
                SegmentFragment(
                  fragment = Fragment(
                    id = -1,
                    start = Some(expectedRouteNode1),
                    end = Some(expectedRouteNode2),
                    way = d.ways(4),
                    nodeSubset = Seq(
                      d.nodes(1),
                      d.nodes(2)
                    )
                  ),
                  reversed = false
                )
              )
            )
          ),
          oneWay = true,
          broken = false,
        ),
        Path(
          start = Some(expectedRouteNode2),
          end = Some(expectedRouteNode3),
          startNodeId = 2,
          endNodeId = 3,
          segments = Seq(
            Segment(
              "paved",
              Seq(
                SegmentFragment(
                  fragment = Fragment(
                    id = -1,
                    start = Some(expectedRouteNode2),
                    end = Some(expectedRouteNode3),
                    way = d.ways(4),
                    nodeSubset = Seq(
                      d.nodes(2),
                      d.nodes(3)
                    )
                  ),
                  reversed = false
                )
              )
            )
          ),
          oneWay = true,
          broken = false
        )
      )
    )
  }

  private def analyze(data: Data, routeNodeInfos: Map[Long, RouteNodeInfo]): RouteAnalysisContext = {

    val loadedRoute = LoadedRoute(
      country = None,
      scopedNetworkType = ScopedNetworkType.rwn,
      data,
      data.relations(1L)
    )

    val context = RouteAnalysisContext(
      new AnalysisContext(),
      loadedRoute,
      orphan = false,
      routeNodeInfos
    )

    SplitNodeRouteAnalyzer.analyze(
      RouteFragmentAnalyzer.analyze(
        RouteNodeAnalyzer.analyze(
          RouteNameAnalyzer.analyze(context)
        )
      )
    )
  }

}