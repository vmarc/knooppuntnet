package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.data.Node
import kpn.api.common.data.Way

import scala.collection.mutable.ListBuffer

class FragmentBuilder {

  val fragments: ListBuffer[Fragment] = ListBuffer[Fragment]()

  def fragment(way: Way, nodes: Node*): SegmentFragment = {
    val f = Fragment.create(None, None, way, nodes.toVector, None)
    fragments += f
    SegmentFragment(f)
  }
}
