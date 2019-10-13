package kpn.server.analyzer.engine.analysis.route.segment

/*
 * Selects the shortest path that is not broken. If there are no paths
 * that are not broken: selects the longest broken path.
 */
object PathSelector {

  def select(paths: Seq[Path]): Option[Path] = {
    val okPaths = paths.filterNot(_.broken)

    if (okPaths.nonEmpty) {
      if(okPaths.size == 1) {
        Some(okPaths.head)
      }
      else {
        Some(sorted(okPaths).head) // shortest
      }
    }
    else {
      val brokenPaths = paths.filter(_.broken)
      if (brokenPaths.nonEmpty) {
        if(brokenPaths.size == 1) {
          Some(brokenPaths.head)
        }
        else {
          Some(sorted(brokenPaths).last) // longest
        }
      }
      else {
        None
      }
    }
  }

  private def sorted(paths: Seq[Path]): Seq[Path] = {
    val s1 = paths.map(path => path.meters -> path)
    val s2 = s1.sortBy(_._1)
    val s3 = s2.map(_._2)
    s3
  }
}
