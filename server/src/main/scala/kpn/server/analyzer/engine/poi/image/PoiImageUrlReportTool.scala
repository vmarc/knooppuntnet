package kpn.server.analyzer.engine.poi.image

import java.net.MalformedURLException
import java.net.URL

object PoiImageUrlReportTool {
  def main(args: Array[String]): Unit = {
    new PoiImageUrlReportTool().report()
  }
}

class PoiImageUrlReportTool {
  def report(): Unit = {
    val poiLinks = PoiLink.linksFromFile("/kpn/pois/poi-links.txt")
    val hosts = poiLinks.flatMap { poiLink =>
      try {
        val host = new URL(poiLink.url).getHost
        if (host.nonEmpty) {
          Some(host)
        }
        else {
          println(s"No host: ${poiLink.toString}")
          None
        }
      }
      catch {
        case e: MalformedURLException =>
          println(s"Malformed url: ${poiLink.toString}")
          None
      }
    }
    val result = hosts.foldLeft(Map.empty[String, Int]) {
      (count, word) => count + (word -> (count.getOrElse(word, 0) + 1))
    }
    result.toSeq.sortBy(_._2).reverse.foreach(println)
  }
}
