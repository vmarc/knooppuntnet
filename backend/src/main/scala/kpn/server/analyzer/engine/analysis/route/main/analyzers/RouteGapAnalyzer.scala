package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.api.common.data.MemberType.Relation
import kpn.api.common.route.RouteStructureRow
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object RouteGapAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteGapAnalyzer(context).analyze()
  }
}

class RouteGapAnalyzer(context: RouteAnalysisContext) {

  def analyze(): RouteAnalysisContext = {
    val structureRows = context.structureRows.zipWithIndex.map { case (row, index) =>
      if (row.memberType == Relation) {
        var gaps: Seq[String] = Seq.empty

        if (index == 0) {
          gaps = gaps :+ "start"
        }
        else {
          val previousRow = context.structureRows(index - 1)
          if (!isConnecting(previousRow, row)) {
            gaps = gaps :+ "top"
          }
        }

        val osmSegmentCount = row.relation.toSeq.flatMap(_.segments).size

        if (osmSegmentCount > 1) {
          gaps = gaps :+ "middle"
        }

        if (index == context.structureRows.size - 1) {
          gaps = gaps :+ "end"
        }
        else {
          val nextRow = context.structureRows(index + 1)
          if (!isConnecting(row, nextRow)) {
            gaps = gaps :+ "bottom"
          }
        }

        val gapString = gaps.mkString("-")
        row.copy(relation = row.relation.map(relation => relation.copy(gaps = Some(gapString))))
      }
      else {
        row
      }
    }
    context.copy(
      _structureRows = Some(structureRows)
    )
  }

  private def isConnecting(row1: RouteStructureRow, row2: RouteStructureRow): Boolean = {
    val segments1 = row1.relation.toSeq.flatMap(_.segments)
    val segments2 = row2.relation.toSeq.flatMap(_.segments)
    segments1.exists { segment1 =>
      segments2.exists { segment2 =>
        segment1.startNodeId == segment2.startNodeId ||
          segment1.startNodeId == segment2.endNodeId ||
          segment1.endNodeId == segment2.startNodeId ||
          segment1.endNodeId == segment2.endNodeId
      }
    }
  }
}
