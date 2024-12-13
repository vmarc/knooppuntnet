package kpn.server.json

import kpn.api.common.NetworkType
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class StringToNetworkTypeConverter extends Converter[String, NetworkType] {
  override def convert(source: String): NetworkType = NetworkType.withName(source)
}
