package kpn.core.poi.configuration

import kpn.api.common.data.Tagable

// TODO redesign - cleanup
class PoiGroupBuilder(name: String, defaultEnabled: Boolean) {

  def poi(name: String, icon: String, minLevel: Long, defaultLevel: Long, xxx: Seq[(String, String)], tagable: Tagable => Boolean): Unit = {
  }
}
