// Migrated to Angular
package kpn.client.common

import kpn.shared.Country
import kpn.shared.EN
import kpn.shared.NL

object Nls {

  def nls(enString: String, nlString: String)(implicit context: Context): String = {
    if (context.language == NL) nlString else enString
  }

  def nlsNL(implicit context: Context): Boolean = {
    context.language == NL
  }

  def nlsEN(implicit context: Context): Boolean = {
    context.language == EN
  }

  def country(country: Option[Country])(implicit context: Context): String = {
    country match {
      case Some(Country.be) => nls("Belgium", "België")
      case Some(Country.nl) => nls("The Netherlands", "Nederland")
      case Some(Country.de) => nls("Germany", "Duitsland")
      case Some(Country.fr) => nls("France", "Frankrijk")
      case Some(Country.at) => nls("Austria", "Oostenrijk")
      case _ => nls("Unknown", "Onbekend")
    }
  }
}
