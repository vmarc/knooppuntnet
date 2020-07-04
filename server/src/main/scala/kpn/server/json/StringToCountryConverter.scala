package kpn.server.json

import kpn.api.custom.Country
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class StringToCountryConverter extends Converter[String, Country] {
  override def convert(source: String): Country = Country.withDomain(source).get
}
