package kpn.server.analyzer.engine.analysis.route.structure

case class ReferenceStructure(wayInfos: Seq[ReferenceWayInfo]) {
  def reportStrings: Seq[String] = {
    wayInfos.zipWithIndex.map { case (wayInfo, index) => s"${index + 1} ${wayInfo.reportString}" }
  }
}
