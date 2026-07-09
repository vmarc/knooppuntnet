package kpn.server.config.converters

import kpn.api.common.RouteType
import org.springframework.core.convert.converter.Converter

class RouteTypeConverter extends Converter[java.lang.String, RouteType] {
  override def convert(source: String): RouteType = {
    RouteType.withName(source)
  }
}
