package kpn.core.db.couch

import akka.util.Timeout
import kpn.core.database.views.common.{Design, View}
import spray.json.JsValue

trait OldDatabase {

  def getRows(
    request: String,
    timeout: Timeout = Couch.defaultTimeout
  ): Seq[JsValue]

  def objectsWithIds( // migrated to docsWithIds
    ids: Seq[String],
    timeout: Timeout = Couch.defaultTimeout,
    stale: Boolean = true
  ): Seq[JsValue]

  def keysWithIds(
    ids: Seq[String],
    timeout: Timeout = Couch.defaultTimeout,
    stale: Boolean = true
  ): Seq[String]

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

  def bulkSave(
    docs: Seq[JsValue]
  ): Unit

}
