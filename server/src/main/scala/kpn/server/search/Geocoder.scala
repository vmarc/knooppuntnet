package kpn.server.search

import kpn.api.common.GeocoderLocation
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

import java.util.Locale.LanguageRange
import scala.jdk.CollectionConverters.*

@Component
class Geocoder {
  def search(query: String): Seq[GeocoderLocation] = {
    val url: String = s"https://nominatim.openstreetmap.org/search?q=$query&format=xml"
    val headers = new HttpHeaders()
    headers.setContentType(MediaType.TEXT_PLAIN)
    headers.setAcceptLanguage(Seq(new LanguageRange("nl")).asJava)
    val entity = new HttpEntity[String]("", headers)
    val restTemplate = new RestTemplate()
    val response: ResponseEntity[String] = restTemplate.exchange(url, HttpMethod.GET, entity, classOf[String])
    NomatimResponseParser.parse(response.getBody)
  }
}
