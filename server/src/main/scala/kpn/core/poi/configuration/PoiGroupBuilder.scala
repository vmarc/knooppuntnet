package kpn.core.poi.configuration

import kpn.api.custom.Tags

class PoiGroupBuilder(name: String, defaultEnabled: Boolean) {

  def poi(name: String, icon: String, minLevel: Long, defaultLevel: Long, xxx: Seq[Tuple2[String, String]], tags: Tags => Boolean): Unit = {
  }

}