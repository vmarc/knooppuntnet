package kpn.core.overpass

import kpn.api.custom.Timestamp

trait OverpassQueryExecutor {

  def execute(queryString: String): String

  def executeQuery(timestamp: Option[Timestamp], query: OverpassQuery): String = {

    val date = timestamp match {
      case Some(utc) => s"""[date:"${utc.iso}"]"""
      case None => ""
    }

    val queryString = query match {
      case q: QueryNodeIds => date + "[timeout:1500][maxsize:24000000000];" + query.string
      case _ => date + "[timeout:500][maxsize:12000000000];" + query.string
    }
    execute(queryString)
  }
}
