package kpn.server.api.monitor.longdistance

import kpn.api.common.monitor.LongdistanceRoutesPage

trait LongdistanceRoutesPageBuilder {

  def build(): Option[LongdistanceRoutesPage]

}
