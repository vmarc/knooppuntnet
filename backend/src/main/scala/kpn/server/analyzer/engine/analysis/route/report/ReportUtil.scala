package kpn.server.analyzer.engine.analysis.route.report

object ReportUtil {
  def osmNodeLink(nodeId: Long): String = {
    osmLink("node", nodeId)
  }

  def osmLink(elementType: String, nodeId: Long): String = {
    s"""<a href="https://www.openstreetmap.org/$elementType/$nodeId">$nodeId</a>"""
  }
}
