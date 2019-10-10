package kpn.core.db.json

import kpn.shared.Country
import spray.json.JsString
import spray.json.JsValue
import spray.json.RootJsonFormat
import spray.json.deserializationError

object CountryFormat extends RootJsonFormat[Country] {

  def write(country: Country): JsValue = JsString(country.domain)

  def read(value: JsValue): Country = {
    value match {
      case domain: JsString => Country.withDomain(domain.value).get
      case _ => deserializationError("Country expected")
    }
  }
}
