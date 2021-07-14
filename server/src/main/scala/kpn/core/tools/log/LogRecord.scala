package kpn.core.tools.log

import nl.basjes.parse.core.Field

class LogRecord {

  var date: String = _
  var time: String = _

  var status: String = _
  var path: String = _
  var userAgent: String = _

  @Field(Array("TIME.DATE:request.receive.time.date_utc"))
  def setDate(value: String): Unit = {
    date = value
  }

  @Field(Array("TIME.TIME:request.receive.time.time_utc"))
  def setTime(value: String): Unit = {
    time = value
  }

  @Field(Array("STRING:request.status.last"))
  def setStatus(value: String): Unit = {
    status = value
  }

  @Field(Array("HTTP.PATH:request.firstline.uri.path"))
  def setPath(value: String): Unit = {
    path = value
  }

  @Field(Array("HTTP.USERAGENT:request.user-agent"))
  def setUserAgent(value: String): Unit = {
    userAgent = value
  }

  def timestamp: String = date + " " + time

  def key: String = timestamp.substring(0, "YYYY-MM-DD HH:M".length) + "0"

}
