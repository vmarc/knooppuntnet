package kpn.server.json

import org.springframework.core.convert.converter.Converter
import kpn.api.custom.NetworkType
import org.springframework.stereotype.Component

@Component
class StringToNetworkTypeConverter extends Converter[String, NetworkType] {
  override def convert(source: String): NetworkType = NetworkType.withName(source).get
}
