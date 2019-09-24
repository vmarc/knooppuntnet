package kpn.core.engine.analysis.location

import kpn.shared.Country

object LocationConfiguration {

  val nl = LocationConfiguration(Country.nl, Seq(3, 4, 8))
  val be = LocationConfiguration(Country.be, Seq(2, 4, 6, 7, 8))
  val de = LocationConfiguration(Country.de, Seq(2, 4, 5, 6))
  val fr = LocationConfiguration(Country.fr, Seq(3, 4, 7))

  val countries: Seq[LocationConfiguration] = Seq(nl, be, de, fr)
}

case class LocationConfiguration(country: Country, levels: Seq[Int])
