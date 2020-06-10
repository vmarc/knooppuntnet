package kpn.core.replicate

import java.io.ByteArrayInputStream
import java.util.zip.GZIPInputStream

import kpn.api.common.ReplicationId
import kpn.core.util.Log
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

import scala.io.Source

class ReplicationRequestExecutorImpl() extends ReplicationRequestExecutor {

  private val URL = "https://planet.osm.org/replication/minute/"
  private val log = Log(classOf[ReplicationRequestExecutorImpl])

  def requestChangesFile(replicationId: ReplicationId): Option[String] = {

    val url = URL + replicationId.name + ".osc.gz"
    val restTemplate = new RestTemplate
    val headers = new HttpHeaders()
    headers.set(HttpHeaders.REFERER, "knooppuntnet.nl")
    val entity = new HttpEntity[String]("", headers)
    try {
      val response: ResponseEntity[Array[Byte]] = restTemplate.exchange(url, HttpMethod.GET, entity, classOf[Array[Byte]])
      response.getStatusCode match {
        case HttpStatus.OK =>
          val gis = new GZIPInputStream(new ByteArrayInputStream(response.getBody))
          val xml = Source.fromInputStream(gis).getLines().mkString("\n")
          Some(xml)
        case httpStatus =>
          log.warn(s"Unexpected http status code $httpStatus (will retry), url=$url")
          None
      }
    }
    catch {
      case e: HttpClientErrorException.NotFound =>
        None
    }
  }

  def requestStateFile(replicationId: ReplicationId): Option[String] = {
    val url = URL + replicationId.name + ".state.txt"
    val restTemplate = new RestTemplate
    val headers = new HttpHeaders()
    headers.set(HttpHeaders.REFERER, "knooppuntnet.nl")
    val entity = new HttpEntity[String]("", headers)
    try {
      val response: ResponseEntity[String] = restTemplate.exchange(url, HttpMethod.GET, entity, classOf[String])
      response.getStatusCode match {
        case HttpStatus.OK =>
          Some(response.getBody)
        case httpStatus =>
          log.warn(s"Unexpected http status code $httpStatus (will retry), url=$url")
          None
      }
    }
    catch {
      case e: HttpClientErrorException.NotFound =>
        None
    }
  }
}
