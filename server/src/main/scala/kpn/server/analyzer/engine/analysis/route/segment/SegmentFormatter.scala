package kpn.server.analyzer.engine.analysis.route.segment

class SegmentFormatter(segment: Segment) {

  def string: String = {
    val fragmentsString = segment.fragments.map(formattedSegmentFragment).mkString
    // s"[${segment.surface}] $fragmentsString"
    fragmentsString
  }

  private def formattedSegmentFragment(fragment: SegmentFragment): String = {
    new SegmentFragmentFormatter(fragment).string
  }

}
