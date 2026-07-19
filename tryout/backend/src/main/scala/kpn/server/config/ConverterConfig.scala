package kpn.server.config

import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.server.config.converters.CountryConverter
import kpn.server.config.converters.RouteTypeConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter

@Configuration
class ConverterConfig {

  @Bean
  def routeTypeConverter: Converter[java.lang.String, RouteType] = {
    new RouteTypeConverter
  }

  @Bean
  def countryConverter: Converter[java.lang.String, Country] = {
    new CountryConverter
  }
}
