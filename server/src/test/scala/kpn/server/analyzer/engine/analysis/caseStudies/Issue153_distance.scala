package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.SharedTestObjects
import kpn.core.util.Haversine
import kpn.core.util.UnitTest

class Issue153_distance extends UnitTest with SharedTestObjects {

  test("confirm the distance between 2 nodes") {
    val node1 = newRawNode(1, latitude = "51.7296532", longitude = "4.8611983") // "
    val node2 = newRawNode(2, latitude = "51.7296870", longitude = "4.8612522") // 60
    val length = Haversine.meters(Seq(node1, node2))
    length should equal(5)
  }

}
