package kpn.server.config.converters

import kpn.api.common.Country
import org.springframework.core.convert.converter.Converter

class CountryConverter extends Converter[java.lang.String, Country] {
  override def convert(source: String): Country = {
    Country.withName(source)
  }
}
