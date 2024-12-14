package kpn.server.analyzer.engine.poi.image

import kpn.api.common.PoiAnalysis
import kpn.api.common.poi.Poi
import kpn.core.tools.config.Dirs
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.api.analysis.pages.poi.PoiAnalysisContext
import kpn.server.api.analysis.pages.poi.analyzers.PoiImageAnalyzer
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.in

import java.io.FileWriter
import java.io.PrintWriter
import scala.io.Source

object PoiImageUrlExportTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new PoiImageUrlExportTool(database).exportPois()
    }
  }
}

class PoiImageUrlExportTool(database: Database) {

  private val batchSize = 1000
  private val log = Log(classOf[PoiImageUrlExportTool])
  private val out = new PrintWriter(new FileWriter(s"${Dirs.root}/pois/poi-links.txt"))

  def exportPois(): Unit = {
    try {
      val allPoiIds = readPoiIds()
      val allPoiIdsSize = allPoiIds.size
      log.info(s"Processing $allPoiIdsSize pois")
      allPoiIds.sliding(batchSize, batchSize).zipWithIndex.foreach { case (poiIds, index) =>
        log.info(s"${index * batchSize}/$allPoiIdsSize")
        exportBatch(poiIds)
      }
    }
    finally {
      out.close()
    }
  }

  private def readPoiIds(): Seq[String] = {
    val source = Source.fromFile(s"${Dirs.root}/pois/pois.txt")
    try {
      source.getLines().toSeq
    }
    finally {
      source.close()
    }
  }

  private def exportBatch(poiIds: Seq[String]): Unit = {
    val pipeline = Seq(
      filter(
        in("_id", poiIds: _*)
      )
    )
    val pois = database.pois.aggregate[Poi](pipeline, log)
    pois.foreach(exportPoi)
  }

  private def exportPoi(poi: Poi): Unit = {
    val context = new PoiImageAnalyzer(
      PoiAnalysisContext(
        poi,
        Seq.empty,
        Seq.empty,
        Seq.empty,
        PoiAnalysis()
      )
    ).analyze

    context.analysis.image match {
      case Some(url) =>
        val result = s"image|${poi.elementType}|${poi.elementId}|$url"
        log.info(result)
        out.println(result)
        out.flush()
      case None =>
    }
  }
}
