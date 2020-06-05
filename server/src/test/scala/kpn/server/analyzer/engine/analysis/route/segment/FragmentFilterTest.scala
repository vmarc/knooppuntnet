package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.Node
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class FragmentFilterTest extends UnitTest with SharedTestObjects {

  test("duplicate fragments from roundabouts should be filtered out") {
    filteredSize(Tags.from("junction" -> "roundabout")) should equal(1)
  }

  test("non-roundabout duplicate fragments with different roles should not be filtered out") {
    filteredSize(Tags.empty) should equal(2)
  }

  private def filteredSize(tags: Tags): Int = {

    val nodes: Seq[Node] = Seq(
      newNode(1001),
      newNode(1002),
      newNode(1003)
    )

    val w = newWay(100, nodes = nodes, tags = tags)

    val fragments: Seq[Fragment] = Seq(
      Fragment.create(None, None, w, nodes, Some("forward")),
      Fragment.create(None, None, w, nodes, Some("backward"))
    )

    FragmentFilter.filter(fragments).size
  }
}
