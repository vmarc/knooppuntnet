package kpn.core.engine.analysis.route.segment

class FragmentFormatter(fragment: Fragment) {

  def string: String = {
    fragment.role match {
      case Some("forward") => s">$details>"
      case Some("backward") => s"<$details<"
      case _ => s"<$details>"
    }
  }

  private def details: String = {
    nodes + fragment.way.id + wayNodes
  }

  private def nodes: String = {
    val startNode = fragment.start.map(_.alternateName)
    val endNode = fragment.end.map(_.alternateName)
    if (startNode.isDefined || endNode.isDefined) {
      startNode.getOrElse("") + "-" + endNode.getOrElse("") + " "
    }
    else {
      ""
    }
  }

  private def wayNodes: String = {
    if (fragment.nodeSubset.nonEmpty) {
       fragment.nodeSubset.map(_.id).mkString("(", "-", ")")
    }
    else {
      ""
    }
  }
}
