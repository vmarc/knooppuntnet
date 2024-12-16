package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import org.apache.commons.codec.digest.DigestUtils

object GeometryDigestAnalyzer extends RouteDetailAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new GeometryDigestAnalyzer(context).analyze
  }
}

class GeometryDigestAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
    val wayNodes = context.allWayNodes.toSeq.flatten
    val string = wayNodes.flatMap(node => Seq(node.latitude, node.longitude)).mkString
    val digest = DigestUtils.sha1Hex(string)
    context.copy(_geometryDigest = Some(digest))
  }
}
