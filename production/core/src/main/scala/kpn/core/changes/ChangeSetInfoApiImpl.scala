package kpn.core.changes

import java.io.File

import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import kpn.core.util.Log
import kpn.shared.changes.ChangeSetInfo
import org.apache.commons.io.FileUtils
import org.xml.sax.SAXParseException
import spray.can.Http
import spray.http.HttpMethods.GET
import spray.http.HttpRequest
import spray.http.HttpResponse
import spray.http.StatusCodes
import spray.http.Uri

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.concurrent.duration.DurationInt
import scala.xml.XML

class ChangeSetInfoApiImpl(directory: File, system: ActorSystem) extends ChangeSetInfoApi {

  private val timeout: Timeout = Timeout(900.seconds)
  private val log = Log(classOf[ChangeSetInfoApiImpl])

  override def get(changeSetId: Long): Option[ChangeSetInfo] = {

    val idString = changeSetId.toString
    val id = idString.substring(idString.length - 2)
    val filename = directory + "/" + id + "/" + changeSetId + ".xml"
    val cachedChangeSetInfoFile = new File(filename)

    if (cachedChangeSetInfoFile.exists) {
      log.debug(s"Changeset $changeSetId resolved from cache")
      val xmlString = FileUtils.readFileToString(cachedChangeSetInfoFile)
      val xml = try {
        XML.loadString(xmlString)
      }
      catch {
        case e: SAXParseException => throw new RuntimeException(s"Could not load xml from $cachedChangeSetInfoFile", e)
      }
      Some(new ChangeSetInfoParser().parse(xml))
    }
    else {
      val uri = Uri("https://www.openstreetmap.org/api/0.6/changeset/" + changeSetId)
      //val headers = List(`Accept-Encoding`(gzip))
      val request = HttpRequest(GET, uri /*, headers*/)

      try {
        val responseFuture: Future[HttpResponse] = (IO(Http)(system).ask(request)(timeout)).mapTo[HttpResponse]
        val response = Await.result(responseFuture, Duration.Inf)
        response.status match {
          case StatusCodes.OK =>
            log.debug(s"Retrieved changeset $changeSetId info from OSM API")
            Thread.sleep(5000)
            val xmlString = response.entity.data.asString
            if (xmlString.contains("<error>Connection to database failed</error>")) {
              log.error("""Exception while fetching changeset, xml contains: "Connection to database failed". Going to sleep for 5 minutes""")
              Thread.sleep(5L * 60 * 1000)
              None
            }
            else {
              FileUtils.writeStringToFile(cachedChangeSetInfoFile, xmlString)
              val xml = XML.loadString(xmlString)
              Some(new ChangeSetInfoParser().parse(xml))
            }

          case _ =>
            log.error(s"Could not retrieve changeset $changeSetId info from OSM API")
            Thread.sleep(5000)
            None
        }
      }
      catch {
        case e: Exception =>
          log.debug("Exception while fetching changeset " + changeSetId, e)
          None
      }
    }
  }
}
