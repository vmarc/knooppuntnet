package kpn.server.json

import kpn.api.common.RouteType
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class StringToRouteTypeConverter extends Converter[String, RouteType] {
  override def convert(source: String): RouteType = RouteType.withName(source)
}
