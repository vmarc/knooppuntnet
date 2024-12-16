package kpn.core.tools.next.support

import kpn.api.custom.Relation
import kpn.core.doc.OldRouteDoc
import kpn.core.doc.RouteDetailDoc
import kpn.core.doc.RouteRelation
import kpn.core.tools.analysis.AnalysisStartConfiguration
import kpn.core.tools.analysis.AnalysisStartToolOptions
import kpn.core.tools.config.Dirs
import kpn.core.tools.next.support.compare.CompareFacts
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.RouteDetailDocBuilder
import org.apache.commons.io.FileUtils

import java.io.File

object RouteAnalysisCompareTool {
  def main(args: Array[String]): Unit = {
    val configuration = new AnalysisStartConfiguration(AnalysisStartToolOptions("kpn-next"))
    new RouteAnalysisCompareTool(configuration).analyze()
  }
}

class RouteAnalysisCompareTool(config: AnalysisStartConfiguration) {

  private val log = Log(classOf[RouteAnalysisTool])

  def analyze(): Unit = {
    log.info("Collecting routeIds")
    val routeIds = config.oldDatabase.oldRoutes.ids()
    val routeIdsSize = routeIds.size
    // val routeIds = readRouteIds("logs/mismatch-ids-7.txt")
    // val routeIds = Seq(5880L)
    log.info(s"Comparing $routeIdsSize routes")
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      if (index % 50 == 0) {
        log.info(s"${index + 1}/$routeIdsSize")
      }
      Log.context(s"${index + 1}/$routeIdsSize route=$routeId") {
        try {
          analyzeAndCompare(routeId)
        } catch {
          case e: Throwable =>
            log.error("Error processing route", e)
        }
      }
    }
    log.info(s"Done")
  }

  private def analyzeAndCompare(routeId: Long): Unit = {
    config.nextRepository.nextRouteRelation(routeId) match {
      case None => log.error(s"route not found in route-relations")
      case Some(nextRouteRelation) =>
        analyzeRoute(nextRouteRelation.relation, nextRouteRelation.structure) match {
          case None => log.error(s"could not analyze route")
          case Some(newRouteDetailDoc) =>
            config.oldDatabase.oldRoutes.findById(routeId) match {
              case None =>
              case Some(oldRouteDoc) =>
                compare(oldRouteDoc, newRouteDetailDoc)
            }
        }
    }
  }

  private def analyzeRoute(relation: Relation, hierarchy: Option[RouteRelation]): Option[RouteDetailDoc] = {
    config.routeDetailMainAnalyzer.analyze(relation, hierarchy).map { context =>
      new RouteDetailDocBuilder(context).build()
    }
  }

  private def compare(oldRouteDoc: OldRouteDoc, newRouteDoc: RouteDetailDoc): Unit = {
    //  new CompareEdges(oldRouteDoc, newRouteDoc, log).compare()
    //  new CompareLabels(oldRouteDoc, newRouteDoc, log).compare()
    new CompareFacts(oldRouteDoc, newRouteDoc, log).compare()
  }

  private def readRouteIds(filename: String): Seq[Long] = {
    val file = new File(Dirs.root, filename)
    FileUtils.readFileToString(file, "UTF-8").split("\n").toSeq.map(_.toLong)
  }
}
