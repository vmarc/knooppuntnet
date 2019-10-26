package kpn.core.database.implementation

import kpn.core.database.views.common.{Design, View}
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.client.support.BasicAuthenticationInterceptor
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

class DatabaseQuery(context: DatabaseContext) {

  def query[T](design: Design, view: View, docType: Class[T], stale: Boolean = true)(args: Any*): T = {

    val restTemplate = new RestTemplate
    restTemplate.getInterceptors.add(
      new BasicAuthenticationInterceptor(context.couchConfig.user, context.couchConfig.password)
    )

    val b = new StringBuilder
    b.append(s"${context.databaseUrl}/_design/${design.name}/_view/${view.name}")
    b.append("?")
    b.append("reduce=false")

    if (args.nonEmpty) {
      val formattedArgs = args.map {
        case string: String => s""""$string""""
        case other => other.toString
      }
      val startKey = formattedArgs.mkString("[", ",", "]")
      val endKey = (formattedArgs :+ "{}").mkString("[", ",", "]")

      b.append(s"&startkey=$startKey")
      b.append(s"&endkey=$endKey")
    }

    if (stale) {
      b.append("&stale=ok")
    }

    val url = b.toString()

    val headers = new HttpHeaders()
    headers.setContentType(MediaType.APPLICATION_JSON)
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE)
    val entity = new HttpEntity[String]("", headers)
    try {
      val response: ResponseEntity[String] = restTemplate.exchange(url, HttpMethod.GET, entity, classOf[String])
      context.objectMapper.readValue(response.getBody, docType)
    }
    catch {
      case e: HttpClientErrorException.BadRequest =>
        throw new IllegalStateException(s"Could not execute query $url\n${e.getStatusText}\n${e.getResponseBodyAsString}", e)
    }

  }
}
