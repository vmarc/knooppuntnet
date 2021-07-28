package kpn.core.database

import kpn.core.database.doc.CouchDoc
import kpn.core.database.implementation.DatabaseAllIds
import kpn.core.database.implementation.DatabaseBulkSave
import kpn.core.database.implementation.DatabaseContext
import kpn.core.database.implementation.DatabaseCreate
import kpn.core.database.implementation.DatabaseDelete
import kpn.core.database.implementation.DatabaseDeleteDocWithId
import kpn.core.database.implementation.DatabaseDeleteDocsWithIds
import kpn.core.database.implementation.DatabaseDocWithId
import kpn.core.database.implementation.DatabaseDocsWithIds
import kpn.core.database.implementation.DatabaseExists
import kpn.core.database.implementation.DatabaseKeysWithIds
import kpn.core.database.implementation.DatabaseQuery
import kpn.core.database.implementation.DatabaseRevision
import kpn.core.database.implementation.DatabaseSave
import kpn.core.database.query.Query
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

class DatabaseImpl(context: DatabaseContext) extends Database {

  override def exists: Boolean = {
    new DatabaseExists(context).exists
  }

  def create(): Unit = {
    new DatabaseCreate(context).create()
  }

  def delete(): Unit = {
    new DatabaseDelete(context).delete()
  }

  override def docWithId[T](docId: String, docType: Class[T]): Option[T] = {
    new DatabaseDocWithId(context).docWithId(docId, docType)
  }

  override def docsWithIds[T](docIds: Seq[String], docType: Class[T], stale: Boolean = true): T = {
    new DatabaseDocsWithIds(context).docsWithIds(docIds, docType, stale)
  }

  override def allIds(stale: Boolean = true): Seq[String] = {
    new DatabaseAllIds(context).execute(stale)
  }

  override def save[T](doc: CouchDoc): Unit = {
    new DatabaseSave(context).save(doc)
  }

  override def bulkSave[T](docs: Seq[CouchDoc]): Unit = {
    new DatabaseBulkSave(context).bulkSave[T](docs)
  }

  override def deleteDocWithId(docId: String): Unit = {
    revision(docId) match {
      case Some(rev) => new DatabaseDeleteDocWithId(context).deleteDocWithId(docId, rev)
      case _ => // ignore, do not have to delete document because it does not exist
    }
  }

  override def deleteDocsWithIds(docIds: Seq[String]): Unit = {
    new DatabaseDeleteDocsWithIds(context).deleteDocsWithIds(docIds)
  }

  override def revision(docId: String): Option[String] = {
    new DatabaseRevision(context).revision(docId)
  }

  override def execute[T](query: Query[T]): T = {
    new DatabaseQuery(context).execute(query)
  }

  override def keysWithIds(docIds: Seq[String], stale: Boolean): Seq[String] = {
    new DatabaseKeysWithIds(context).keysWithIds(docIds, stale)
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
