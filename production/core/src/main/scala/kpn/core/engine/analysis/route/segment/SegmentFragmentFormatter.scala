package kpn.core.engine.analysis.route.segment

class SegmentFragmentFormatter(segmentFragment: SegmentFragment) {

  def string: String = {
    val prefix = if (segmentFragment.reversed) "-" else "+"
    val fragment = new FragmentFormatter(segmentFragment.fragment).string
    prefix + fragment
  }
}
