package kpn.core.db.couch

import akka.util.Timeout
import kpn.core.db.views.Design
import kpn.core.db.views.View
import spray.json.JsValue

trait Database {

  def exists: Boolean

  def create(): Unit

  def delete(): Unit

  def delete(id: String): Unit

  def deleteDocs(ids: Seq[String]): Unit

  def getJsValue(
    request: String,
    timeout: Timeout = Couch.defaultTimeout
  ): JsValue

  def getJsonString(
    request: String,
    timeout: Timeout = Couch.defaultTimeout
  ): String

  def getRows(
    request: String,
    timeout: Timeout = Couch.defaultTimeout
  ): Seq[JsValue]

  def objectsWithIds(
    ids: Seq[String],
    timeout: Timeout = Couch.defaultTimeout,
    stale: Boolean = true
  ): Seq[JsValue]

  def keysWithIds(
    ids: Seq[String],
    timeout: Timeout = Couch.defaultTimeout,
    stale: Boolean = true
  ): Seq[String]

  def optionGet(
    request: String,
    timeout: Timeout = Couch.defaultTimeout
  ): Option[JsValue]

  def optionGets(
    request: String,
    timeout: Timeout = Couch.defaultTimeout
  ): Option[String]

  def query(
    design: Design,
    view: View,
    timeout: Timeout = Couch.defaultTimeout,
    stale: Boolean = true
  )(args: Any*): Seq[JsValue]

  def pagingQuery(
    design: Design,
    view: View,
    timeout: Timeout = Couch.defaultTimeout,
    stale: Boolean = true,
    limit: Int = 999999,
    skip: Int = 0
  ): PagingQueryResult

  def docs(
    startKey: String,
    endKey: String,
    timeout: Timeout = Couch.defaultTimeout,
    limit: Int = 9999
  ): Seq[JsValue]

  def keys(
    startKey: String,
    endKey: String,
    timeout: Timeout = Couch.defaultTimeout
  ): Seq[JsValue]

  def groupQuery(
    groupLevel: Int,
    design: Design,
    view: View,
    timeout: Timeout = Couch.defaultTimeout,
    stale: Boolean = true
  )(args: Any*): Seq[JsValue]

  def save(
    id: String,
    value: JsValue
  ): Unit

  def authorizedSsave(
    id: String,
    value: JsValue
  ): Unit

  def currentRevision(
    id: String,
    timeout: Timeout = Couch.defaultTimeout
  ): Option[String]

  def bulkSave(
    docs: Seq[JsValue]
  ): Unit

}
