package kpn.server.api.monitor

import kpn.api.common.monitor.LongdistanceRoutesPage

trait LongdistanceRoutesPageBuilder {

  def build(): Option[LongdistanceRoutesPage]

}
