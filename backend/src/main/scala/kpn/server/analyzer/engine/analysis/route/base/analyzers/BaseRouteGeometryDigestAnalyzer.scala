package kpn.server.analyzer.engine.analysis.route.base.analyzers

import org.apache.commons.codec.digest.DigestUtils

object BaseRouteGeometryDigestAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteGeometryDigestAnalyzer(context).analyze
  }
}

class BaseRouteGeometryDigestAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {
    val wayNodes = context.allWayNodes.toSeq.flatten
    val string = wayNodes.flatMap(node => Seq(node.latitude, node.longitude)).mkString
    val digest = DigestUtils.sha1Hex(string)
    context.copy(_geometryDigest = Some(digest))
  }
}
