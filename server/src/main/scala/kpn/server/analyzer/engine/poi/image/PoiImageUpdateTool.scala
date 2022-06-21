package kpn.server.analyzer.engine.poi.image

import kpn.api.common.PoiState
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo

import java.net.MalformedURLException
import java.net.URL
import scala.annotation.tailrec

object PoiImageUpdateTool {
  def main(args: Array[String]): Unit = {

    System.setProperty("sun.net.client.defaultConnectTimeout", "15000")
    System.setProperty("sun.net.client.defaultReadTimeout", "15000")

    Mongo.executeIn("kpn-prod") { database =>
      new PoiImageUpdateTool(database).update()
    }
  }
}

class PoiImageUpdateTool(database: Database) {

  private val log = Log(classOf[PoiImageUpdateTool])
  private val imageRepo = new PoiImageRepositoryImpl()
  private val retriever = new PoiImageRetrieverImpl(imageRepo)
  private var poiCount = 0
  private var poiNumber = 0

  def update(): Unit = {
    val poiLinks = PoiLink.linksFromFile("/kpn/pois/poi-links.txt")
    val todoPoiLinks = poiLinks.filter(poiLink => !imageRepo.exists(poiLink.toRef))
    poiCount = todoPoiLinks.size
    val urlsByHost = buildUrlsByHost(poiLinks)
    update(urlsByHost)
  }

  @tailrec
  private def update(urlsByHost: Map[String, Seq[PoiLink]]): Unit = {
    val remainingUrlsByHost = urlsByHost.flatMap { case (host, poiLinks) =>
      if (poiLinks.isEmpty) {
        None
      }
      else {
        val poiLink = poiLinks.head
        database.poiStates.save(update(poiLink))
        val remaining = poiLinks.tail
        if (remaining.nonEmpty) {
          Some((host, remaining))
        }
        else {
          None
        }
      }
    }
    Thread.sleep(5 * 1000)
    update(remainingUrlsByHost)
  }

  private def update(poiLink: PoiLink): PoiState = {
    poiNumber = poiNumber + 1
    Log.context(s"$poiNumber/$poiCount") {
      retriever.retrieveImage(poiLink.toRef, poiLink.url)
    }
  }

  private def buildUrlsByHost(poiLinks: Seq[PoiLink]): Map[String, Seq[PoiLink]] = {
    poiLinks.flatMap { poiLink =>
      Log.context(poiLink.toRef.toId) {
        try {
          val host = new URL(poiLink.url).getHost
          if (host.nonEmpty) {
            Some(host -> poiLink)
          }
          else {
            log.warn(s"No host: ${poiLink.toString}")
            database.poiStates.save(
              PoiState(
                _id = poiLink.toRef.toId,
                imageStatus = Some("NoHost")
              )
            )
            None
          }
        }
        catch {
          case e: MalformedURLException =>
            log.warn(s"Malformed url: ${poiLink.url}")
            database.poiStates.save(
              PoiState(
                _id = poiLink.toRef.toId,
                imageStatus = Some("MalformedUrl")
              )
            )
            None
        }
      }
    }.groupBy(_._1).map(x => x._1 -> x._2.map(_._2))
  }
}
