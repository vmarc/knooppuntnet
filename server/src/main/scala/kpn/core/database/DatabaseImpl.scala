package kpn.core.database

import kpn.core.database.doc.CouchDoc
import kpn.core.database.implementation.DatabaseContext
import kpn.core.database.implementation.DatabaseDocWithId
import kpn.core.database.implementation.DatabaseQuery
import kpn.core.database.implementation.DatabaseSave
import kpn.core.database.query.Query
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

class DatabaseImpl(context: DatabaseContext) extends Database {

  override def docWithId[T](docId: String, docType: Class[T]): Option[T] = {
    new DatabaseDocWithId(context).docWithId(docId, docType)
  }

  override def save[T](doc: CouchDoc): Unit = {
    new DatabaseSave(context).save(doc)
  }

  override def deleteDocWithId(docId: String): Unit = {
  }

  override def execute[T](query: Query[T]): T = {
    new DatabaseQuery(context).execute(query)
  }

  override def post[T](query: Query[T], body: String, docType: Class[T]): T = {
    val url: String = s"${context.databaseUrl}/${query.build()}"
    val headers = new HttpHeaders()
    headers.setContentType(MediaType.APPLICATION_JSON)
    val entity = new HttpEntity[String](body, headers)
    val response: ResponseEntity[String] = context.restTemplate.exchange(url, HttpMethod.POST, entity, classOf[String])
    context.objectMapper.readValue(response.getBody, docType)
  }

}
