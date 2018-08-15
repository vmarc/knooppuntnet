package kpn.core.db.json

import kpn.shared.Country
import spray.json.JsString
import spray.json.JsValue
import spray.json.RootJsonFormat
import spray.json.deserializationError

object CountryOptionFormat extends RootJsonFormat[Option[Country]] {

  def write(country: Option[Country]): JsValue = JsString(country.map(_.domain).getOrElse(""))

  def read(value: JsValue): Option[Country] = {
    value match {
      case domain: JsString => Country.withDomain(domain.value)
      case _ => deserializationError("Country expected")
    }
  }
}
