package kpn.core.overpass

import kpn.api.custom.Timestamp

object OverpassQueryExecutor {
  val defaultTimeout: Option[Long] = Some(500)
  val defaultMaxSize: Option[Long] = Some(12000000000L)
}

trait OverpassQueryExecutor {

  def timeout: Option[Long] = OverpassQueryExecutor.defaultTimeout

  def maxSize: Option[Long] = OverpassQueryExecutor.defaultMaxSize

  def execute(queryString: String): String

  def executeQuery(timestamp: Option[Timestamp], query: OverpassQuery): String = {

    val dateString = timestamp match {
      case Some(utc) => s"""[date:"${utc.iso}"]"""
      case None => ""
    }

    val timeoutString = query.timeout match {
      case Some(queryTimeout) => s"[timeout:$queryTimeout]"
      case None =>
        timeout match {
          case Some(executorTimeout) => s"[timeout:$executorTimeout]"
          case None => ""
        }
    }

    val maxSizeString = query.maxSize match {
      case Some(queryMaxSize) => s"[maxsize:$queryMaxSize]"
      case None =>
        maxSize match {
          case Some(executorMaxSize) => s"[maxsize:$executorMaxSize]"
          case None => ""
        }
    }

    val separator = if (dateString.nonEmpty || timeoutString.nonEmpty && maxSizeString.nonEmpty) ";" else ""

    val queryString = s"$dateString$timeoutString$maxSizeString$separator${query.string}"
    execute(queryString)
  }
}
