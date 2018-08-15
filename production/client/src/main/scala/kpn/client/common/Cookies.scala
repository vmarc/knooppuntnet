package kpn.client.common

import org.scalajs.dom

import scala.scalajs.js.URIUtils

object Cookies {

  def cookies: Map[String, String] = {
    parse(dom.document.cookie)
  }

  def parse(cookieString: String): Map[String, String] = {
    val keysAndValues = cookieString.split(";")
    keysAndValues.flatMap { keyAndValue =>
      keyAndValue.split("=") match {
        case Array(key, value) => Some(key -> URIUtils.decodeURIComponent(value))
        case _ => None
      }
    }.toMap
  }
}
