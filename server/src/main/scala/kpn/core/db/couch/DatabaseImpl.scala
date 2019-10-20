package kpn.core.db.couch

import com.fasterxml.jackson.databind.ObjectMapper
import kpn.core.db.Doc
import kpn.core.db.views.Design
import kpn.core.db.views.View
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.client.support.BasicAuthenticationInterceptor
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

class DatabaseImpl(couchConfig: CouchConfig, objectMapper: ObjectMapper, name: String) extends Database {

  override def exists: Boolean = {
    val restTemplate = new RestTemplate
    try {
      restTemplate.headForHeaders(databaseUrl)
      true
    }
    catch {
      case e: HttpClientErrorException =>
        if (HttpStatus.NOT_FOUND.equals(e.getStatusCode)) {
          false
        }
        else {
          throw e
        }
    }
  }

  def create(): Unit = {
    val restTemplate = new RestTemplate
    restTemplate.getInterceptors.add(
      new BasicAuthenticationInterceptor(couchConfig.user, couchConfig.password)
    )

    try {
      restTemplate.put(databaseUrl, "")
    }
    catch {
      case e: ResourceAccessException =>
        throw new IllegalStateException(s"Could not create database '$databaseUrl' (invalid user/password?)", e)

      case e: HttpClientErrorException =>
        if (HttpStatus.PRECONDITION_FAILED.equals(e.getStatusCode)) {
          throw new IllegalStateException(s"Could not create database '$databaseUrl' (already exists?)", e)
        }
        else {
          throw e;
        }
    }
  }

  def delete(): Unit = {
    val restTemplate = new RestTemplate
    restTemplate.getInterceptors.add(
      new BasicAuthenticationInterceptor(couchConfig.user, couchConfig.password)
    )

    try {
      restTemplate.delete(databaseUrl)
    }
    catch {
      case e: ResourceAccessException =>
        throw new IllegalStateException(s"Could not delete database '$databaseUrl' (invalid user/password?)", e)

      case e: HttpClientErrorException =>
        if (HttpStatus.NOT_FOUND.equals(e.getStatusCode)) {
          throw new IllegalStateException(s"Could not delete database '$databaseUrl' (database does not exist?)", e)
        }
        else {
          throw e
        }
    }
  }

  override def docWithId[T](id: String, docType: Class[T]): Option[T] = {
    val restTemplate = new RestTemplate
    try {
      val response = restTemplate.getForObject(s"$databaseUrl/$id", classOf[String])
      Some(objectMapper.readValue(response, docType))
    }
    catch {
      case e: ResourceAccessException =>
        throw new IllegalStateException(s"Could not get document '$databaseUrl' (invalid user/password?)", e)

      case e: HttpClientErrorException =>
        if (HttpStatus.NOT_FOUND.equals(e.getStatusCode)) {
          None
        }
        else {
          throw e
        }
    }
  }

  override def docsWithIds[T](docIds: Seq[String], docType: Class[T], stale: Boolean = true): T = {

    val body = objectMapper.writeValueAsString(Keys(docIds))

    val restTemplate = new RestTemplate
    restTemplate.getInterceptors.add(
      new BasicAuthenticationInterceptor(couchConfig.user, couchConfig.password)
    )

    val b = UriComponentsBuilder.fromHttpUrl(s"$databaseUrl/_all_docs")
    b.queryParam("include_docs", "true")
    if (stale) {
      b.queryParam("stale", "ok")
    }
    val url = b.toUriString

    val headers = new HttpHeaders()
    headers.setContentType(MediaType.APPLICATION_JSON)
    val entity = new HttpEntity[String](body, headers)
    val response: ResponseEntity[String] = restTemplate.exchange(url, HttpMethod.POST, entity, classOf[String])
    objectMapper.readValue(response.getBody, docType)
  }

  override def save[T](doc: Doc): Unit = {

    val url = s"$databaseUrl/${doc._id}"
    val json = objectMapper.writeValueAsString(doc)

    val restTemplate = new RestTemplate
    restTemplate.getInterceptors.add(
      new BasicAuthenticationInterceptor(couchConfig.user, couchConfig.password)
    )

    try {
      val entity = new HttpEntity[String](json)
      val response: ResponseEntity[String] = restTemplate.exchange(url, HttpMethod.PUT, entity, classOf[String])
      if (!HttpStatus.CREATED.equals(response.getStatusCode)) {
        throw new IllegalStateException(s"Could not save '$url' (unexpected status code ${response.getStatusCode.name()})")
      }
    }
    catch {
      case e: ResourceAccessException =>
        throw new IllegalStateException(s"Could not save '$url' (invalid user/password?)", e)

      case e: HttpClientErrorException =>
        if (HttpStatus.CONFLICT.equals(e.getStatusCode)) {
          throw new IllegalStateException(s"Could not save '$url' (_rev mismatch)", e)
        }
        if (HttpStatus.UNAUTHORIZED.equals(e.getStatusCode)) {
          throw new IllegalStateException(s"Could not save '$url' (invalid user/password?)", e)
        }
        throw new IllegalStateException(s"Could not save '$url'", e)
    }
  }

  override def revision(docId: String): Option[String] = {

    val url = s"$databaseUrl/${docId}"

    val restTemplate = new RestTemplate
    restTemplate.getInterceptors.add(
      new BasicAuthenticationInterceptor(couchConfig.user, couchConfig.password)
    )

    try {
      val entity = new HttpEntity[String]("")
      val response: ResponseEntity[String] = restTemplate.exchange(url, HttpMethod.HEAD, entity, classOf[String])
      Some(withoutQuotes(response.getHeaders.getETag))
    }
    catch {
      case e: ResourceAccessException =>
        throw new IllegalStateException(s"Could not get document revision '$databaseUrl' (invalid user/password?)", e)

      case e: HttpClientErrorException =>
        if (HttpStatus.NOT_FOUND.equals(e.getStatusCode)) {
          None
        }
        else {
          throw e
        }
    }
  }

  def query[T](design: Design, view: View, docType: Class[T], stale: Boolean = true)(args: Any*): T = {

    val restTemplate = new RestTemplate
    restTemplate.getInterceptors.add(
      new BasicAuthenticationInterceptor(couchConfig.user, couchConfig.password)
    )

    val b = new StringBuilder
    b.append(s"$databaseUrl/_design/${design.name}/_view/${view.name}")
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
      objectMapper.readValue(response.getBody, docType)
    }
    catch {
      case e: HttpClientErrorException.BadRequest =>
        throw new IllegalStateException(s"Could not execute query $url\n${e.getStatusText}\n${e.getResponseBodyAsString}", e)
    }

  }

  private def databaseUrl: String = {
    s"http://${couchConfig.host}:${couchConfig.port}/${name}"
  }

  private def withoutQuotes(string: String): String = {
    if (string.startsWith("\"") && string.endsWith("\"")) {
      string.substring(1, string.length - 1)
    }
    else {
      string
    }
  }

}
