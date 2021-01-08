package kpn.core.tools.log

import nl.basjes.parse.core.Field

class LogRecord {

  var status: String = null
  var path: String = null
  var userAgent: String = null

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

}
