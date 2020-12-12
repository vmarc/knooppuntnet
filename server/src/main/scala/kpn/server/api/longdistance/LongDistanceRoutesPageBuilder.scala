package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRoutesPage

trait LongDistanceRoutesPageBuilder {

  def build(): Option[LongDistanceRoutesPage]

}
