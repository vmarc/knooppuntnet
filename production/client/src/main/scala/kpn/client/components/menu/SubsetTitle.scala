// TODO migrate to Angular
package kpn.client.components.menu

import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.shared.Country
import kpn.shared.NetworkType
import kpn.shared.Subset

class SubsetTitle(implicit val context: Context) {

  def get(subset: Subset): String = {
    networkType(subset) + " in " + country(subset)
  }

  private def networkType(subset: Subset): String = {
    subset.networkType match {
      case NetworkType.hiking => nls("Walking", "Wandelen")
      case NetworkType.bicycle => nls("Cycling", "Fietsen")
      case NetworkType.horseRiding => nls("Horse riding", "Ruiter")
      case NetworkType.motorboat => nls("Motorboat", "Motorboot")
      case NetworkType.canoe => nls("Canoe", "Kano")
      case NetworkType.inlineSkates => "Inline skates"
    }
  }

  private def country(subset: Subset): String = {
    country(subset.country)
  }

  def country(country: Country): String = {
    country match {
      case Country.be => nls("Belgium", "België")
      case Country.nl => nls("The Netherlands", "Nederland")
      case Country.de => nls("Germany", "Duitsland")
      case Country.fr => nls("France", "Germany")
    }
  }
}
