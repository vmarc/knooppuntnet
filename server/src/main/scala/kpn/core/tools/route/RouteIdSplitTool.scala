package kpn.core.tools.route

import kpn.core.util.Log
import org.apache.commons.io.FileUtils

import java.io.File

object RouteIdSplitTool {
  def main(args: Array[String]): Unit = {
    new RouteIdSplitTool().split()
  }
}

class RouteIdSplitTool {

  private val log = Log(classOf[RouteIdSplitTool])
  private val routeFileAnalyzer = new RouteFileAnalyzerImpl()

  def split(): Unit = {
    val routeIds = readAllRouteIds().sorted
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      Log.context(s"${index + 1}/${routeIds.size}") {
        processRoute(routeId)
      }
    }
    log.info("done")
  }

  private def processRoute(routeId: Long): Unit = {
    val formattedRouteId = String.format("%8d", routeId)
    Log.context(formattedRouteId) {
      try {
        routeFileAnalyzer.analyze(routeId) match {
          case None =>
          case Some(newRouteAnalysis) =>
            newRouteAnalysis.route.summary.country match {
              case None =>
              case Some(country) =>
                val networkType = newRouteAnalysis.route.summary.networkType
                val subset = s"${networkType.name}-${country.domain}"
                val filename = s"/kpn/routes/route-ids-$subset.txt"
                FileUtils.writeStringToFile(new File(filename), s"$routeId\n", "UTF-8", true)
                log.info(subset)
            }
        }
      }
      catch {
        case e: Exception =>
          log.error("Could not process route", e)
          throw e
      }
    }
  }

  private def readAllRouteIds(): Seq[Long] = {
    val subdirs = listDirs(new File("/kpn/routes"))
    subdirs.flatMap { subdir =>
      val files = listFiles(subdir)
      files.map(_.getName.dropRight(4).toLong)
    }
  }

  private def listDirs(dir: File): Seq[File] = {
    val files = dir.listFiles().filter(_.isDirectory)
    files.sortBy(_.getAbsolutePath)
  }

  private def listFiles(dir: File): Seq[File] = {
    val files = dir.listFiles().filter(_.getName.endsWith(".xml"))
    files.sortBy(_.getName.dropRight(4).toLong)
  }
}
