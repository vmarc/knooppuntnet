package kpn.server.analyzer.engine.changes.changes

import java.io.File
import java.nio.charset.Charset

import kpn.api.common.changes.ChangeSetInfo
import kpn.core.util.Log
import org.apache.commons.io.FileUtils
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.xml.sax.SAXParseException

import scala.xml.XML

class ChangeSetInfoApiImpl(directory: File) extends ChangeSetInfoApi {

  private val log = Log(classOf[ChangeSetInfoApiImpl])

  override def get(changeSetId: Long): Option[ChangeSetInfo] = {

    val idString = changeSetId.toString
    val id = idString.substring(idString.length - 2)
    val filename = directory + "/" + id + "/" + changeSetId + ".xml"
    val cachedChangeSetInfoFile = new File(filename)

    if (cachedChangeSetInfoFile.exists) {
      log.debug(s"Changeset $changeSetId resolved from cache")
      val xmlString = FileUtils.readFileToString(cachedChangeSetInfoFile, "UTF-8")
      val xml = try {
        XML.loadString(xmlString)
      }
      catch {
        case e: SAXParseException => throw new RuntimeException(s"Could not load xml from $cachedChangeSetInfoFile", e)
      }
      Some(new ChangeSetInfoParser().parse(xml))
    }
    else {

      val restTemplate = new RestTemplate
      val url = s"https://www.openstreetmap.org/api/0.6/changeset/$changeSetId"

      val headers = new HttpHeaders()
      headers.setAccept(java.util.Arrays.asList(MediaType.TEXT_XML))
      headers.setAcceptCharset(java.util.Arrays.asList(Charset.forName("UTF-8")))
      headers.set(HttpHeaders.REFERER, "knooppuntnet.nl")
      val entity = new HttpEntity[String]("", headers)

      try {
        val response: ResponseEntity[String] = restTemplate.exchange(url, HttpMethod.GET, entity, classOf[String])
        response.getStatusCode match {
          case HttpStatus.OK =>
            log.debug(s"Retrieved changeset $changeSetId info from OSM API")
            Thread.sleep(5000)
            val xmlString = response.getBody
            if (xmlString.contains("<error>Connection to database failed</error>")) {
              log.error("""Exception while fetching changeset, xml contains: "Connection to database failed". Going to sleep for 5 minutes""")
              Thread.sleep(5L * 60 * 1000)
              None
            }
            else {
              FileUtils.writeStringToFile(cachedChangeSetInfoFile, xmlString, "UTF-8")
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
