package kpn.api.custom

import kpn.api.common.Country
import kpn.api.common.RouteType

object Subset {

  val beHiking: Subset = Subset(Country.be, RouteType.hiking)
  val beCycling: Subset = Subset(Country.be, RouteType.cycling)
  val beHorseRiding: Subset = Subset(Country.be, RouteType.horseRiding)

  val nlHiking: Subset = Subset(Country.nl, RouteType.hiking)
  val nlCycling: Subset = Subset(Country.nl, RouteType.cycling)
  val nlHorseRiding: Subset = Subset(Country.nl, RouteType.horseRiding)
  val nlCanoe: Subset = Subset(Country.nl, RouteType.canoe)
  val nlMotorboat: Subset = Subset(Country.nl, RouteType.motorboat)
  val nlInlineSkates: Subset = Subset(Country.nl, RouteType.inlineSkating)

  val deHiking: Subset = Subset(Country.de, RouteType.hiking)
  val deCycling: Subset = Subset(Country.de, RouteType.cycling)
  val deHorseRiding: Subset = Subset(Country.de, RouteType.horseRiding)

  val frHiking: Subset = Subset(Country.fr, RouteType.hiking)
  val frCycling: Subset = Subset(Country.fr, RouteType.cycling)
  val frHorseRiding: Subset = Subset(Country.fr, RouteType.horseRiding)
  val frCanoe: Subset = Subset(Country.fr, RouteType.canoe)

  val atCycling: Subset = Subset(Country.at, RouteType.cycling)

  val esHiking: Subset = Subset(Country.es, RouteType.hiking)
  val esCycling: Subset = Subset(Country.es, RouteType.cycling)

  val dkCycling: Subset = Subset(Country.dk, RouteType.cycling)

  val plHiking: Subset = Subset(Country.pl, RouteType.hiking)
  val plCycling: Subset = Subset(Country.pl, RouteType.cycling)

  val all: Seq[Subset] = Seq(
    nlCycling,
    beCycling,
    deCycling,
    frCycling,
    atCycling,
    esCycling,
    dkCycling,
    plCycling,
    nlHiking,
    beHiking,
    deHiking,
    frHiking,
    esHiking,
    plHiking,
    nlHorseRiding,
    beHorseRiding,
    deHorseRiding,
    frHorseRiding,
    nlCanoe,
    frCanoe,
    nlMotorboat,
    nlInlineSkates
  )

  def ofName(domain: String, routeTypeName: String): Option[Subset] = {
    all.find(s => s.country.entryName == domain && s.routeType.entryName == routeTypeName)
  }

  def of(country: Country, routeType: RouteType): Option[Subset] = {
    all.find(s => s.country == country && s.routeType == routeType)
  }
}

case class Subset(country: Country, routeType: RouteType) extends Ordered[Subset] {

  def key: String = s"${country.entryName}:${routeType.entryName}"

  def name: String = s"${country.entryName}-${routeType.entryName}"

  def string: String = s"${country.entryName}/${routeType.entryName}"

  import scala.math.Ordered.orderingToOrdered

  def compare(that: Subset): Int = (this.country.entryName, this.routeType.entryName).compare((that.country.entryName, that.routeType.entryName))
}
