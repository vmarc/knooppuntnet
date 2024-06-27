package kpn.server.analyzer.engine.analysis.route

import kpn.core.doc.RouteDetailDoc
import kpn.core.doc.RouteDoc

trait RouteMainAnalyzer {

  def analyze(routeDetailDoc: RouteDetailDoc): Option[RouteDoc]
}
