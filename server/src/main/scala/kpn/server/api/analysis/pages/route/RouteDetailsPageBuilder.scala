package kpn.server.api.analysis.pages.route

import kpn.api.common.Language
import kpn.api.common.route.RouteDetailsPage

trait RouteDetailsPageBuilder {

  def build(language: Language, routeId: Long): Option[RouteDetailsPage]

}
