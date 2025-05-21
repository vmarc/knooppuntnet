package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeName
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Tags
import kpn.api.custom.Tags.TagTuple
import kpn.core.test.SharedTestObjects
import kpn.core.util.UnitTest

class NodeNameAnalyzerTest extends UnitTest with SharedTestObjects {

  test("rwn_ref") {
    assertEqual(
      analyze("rwn_ref" -> "01"),
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("proposed:rwn_ref") {
    assertEqual(
      analyze("proposed:rwn_ref" -> "01"),
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
          longName = None,
          proposed = true
        )
      )
    )
  }

  test("rwn_ref and rwn_name") {
    assertEqual(
      analyze(
        "rwn_ref" -> "01",
        "rwn_name" -> "01"
      ),
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("add leading zero to numeric tag value to normalize node name") {
    assertEqual(
      analyze("rwn_ref" -> "1"), // node leading zero in tag
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01", // <- leading zero added
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("multiple names 01 / 02 - different network types") {
    assertEqual(
      analyze(
        "rwn_ref" -> "01",
        "rcn_ref" -> "02"
      ),
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
          longName = None,
          proposed = false
        ),
        NodeName(
          routeType = RouteType.cycling,
          routeScope = RouteScope.regional,
          name = "02",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("multiple names - proposed and not proposed") {
    assertEqual(
      analyze(
        "proposed:rwn_ref" -> "01",
        "rcn_ref" -> "02"
      ),
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
          longName = None,
          proposed = true
        ),
        NodeName(
          routeType = RouteType.cycling,
          routeScope = RouteScope.regional,
          name = "02",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("same name in multiple network scopes") {
    assertEqual(
      analyze(
        "rwn_ref" -> "01",
        "lwn_ref" -> "01"
      ),
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.local,
          name = "01",
          longName = None,
          proposed = false
        ),
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("same name in multiple network scopes and proposed and not proposed") {
    assertEqual(
      analyze(
        "proposed:lwn_ref" -> "01",
        "rwn_ref" -> "01"
      ),
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.local,
          name = "01",
          longName = None,
          proposed = true
        ),
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("same name in multiple network types and scopes") {
    assertEqual(
      analyze(
        "rwn_ref" -> "01",
        "lwn_ref" -> "01",
        "rcn_ref" -> "01"
      ),
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.local,
          name = "01",
          longName = None,
          proposed = false
        ),
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
          longName = None,
          proposed = false
        ),
        NodeName(
          routeType = RouteType.cycling,
          routeScope = RouteScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("name - empty string when no name") {
    assertEqual(
      analyze(),
      Seq.empty
    )
  }

  test("rwn_name") {
    assertEqual(
      analyze(
        "rwn_name" -> "01"
      ),
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("proposed:rwn_name") {
    assertEqual(
      analyze(
        "proposed:rwn_name" -> "01"
      ),
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
          longName = None,
          proposed = true
        )
      )
    )
  }

  test("names - name:rwn_ref") {
    assertEqual(
      analyze(
        "rwn_ref" -> "01",
        "name:rwn_ref" -> "long name"
      ),
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
          longName = Some("long name"),
          proposed = false
        )
      )
    )
  }

  test("??n_ref") {
    ScopedRouteType.all.foreach { scopedRouteType =>
      assertEqual(
        analyze(
          scopedRouteType.nodeRefTagKey -> "01"
        ),
        Seq(
          NodeName(
            routeType = scopedRouteType.routeType,
            routeScope = scopedRouteType.routeScope,
            name = "01",
            longName = None,
            proposed = false
          )
        )
      )
    }
  }

  test("??n_name") {
    ScopedRouteType.all.foreach { scopedRouteType =>
      assertEqual(
        analyze(
          scopedRouteType.nodeNameTagKey -> "01"
        ),
        Seq(
          NodeName(
            routeType = scopedRouteType.routeType,
            routeScope = scopedRouteType.routeScope,
            name = "01",
            longName = None,
            proposed = false
          )
        )
      )
    }
  }

  test("proposed:??n_ref") {
    ScopedRouteType.all.foreach { scopedRouteType =>
      assertEqual(
        analyze(
          scopedRouteType.proposedNodeRefTagKey -> "01"
        ),
        Seq(
          NodeName(
            routeType = scopedRouteType.routeType,
            routeScope = scopedRouteType.routeScope,
            name = "01",
            longName = None,
            proposed = true
          )
        )
      )
    }
  }

  test("propsed:??n_name") {
    ScopedRouteType.all.foreach { scopedRouteType =>
      assertEqual(
        analyze(
          scopedRouteType.proposedNodeNameTagKey -> "01"
        ),
        Seq(
          NodeName(
            routeType = scopedRouteType.routeType,
            routeScope = scopedRouteType.routeScope,
            name = "01",
            longName = None,
            proposed = true
          )
        )
      )
    }
  }

  test("multiple scopes and network types") {
    assertEqual(
      analyze(
        "network:type" -> "node_network",
        "rwn_ref" -> "01",
        "lwn_ref" -> "02",
        "rcn_ref" -> "03"
      ).sortBy(_.name),
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
          longName = None,
          proposed = false
        ),
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.local,
          name = "02",
          longName = None,
          proposed = false
        ),
        NodeName(
          routeType = RouteType.cycling,
          routeScope = RouteScope.regional,
          name = "03",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("state=proposed") {
    assertEqual(
      analyze(
        "network:type" -> "node_network",
        "rwn_ref" -> "01",
        "state" -> "proposed"
      ),
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
          longName = None,
          proposed = true
        )
      ),
    )
  }

  private def analyze(tags: TagTuple*): Seq[NodeName] = {
    val nodeTags = Tags.from(tags: _*).toSeq ++ Tags.from("network:type" -> "node_network")
    val node = newNode(tags = nodeTags)
    NodeNameAnalyzer.analyze(node)
  }
}
