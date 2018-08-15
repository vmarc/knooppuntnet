package kpn.shared

object Subset {

  val beHiking = Subset(Country.be, NetworkType.hiking)
  val beBicycle = Subset(Country.be, NetworkType.bicycle)
  val nlHiking = Subset(Country.nl, NetworkType.hiking)
  val nlBicycle = Subset(Country.nl, NetworkType.bicycle)
  val nlHorse = Subset(Country.nl, NetworkType.horse)
  val nlCanoe = Subset(Country.nl, NetworkType.canoe)
  val nlMotorboat = Subset(Country.nl, NetworkType.motorboat)
  val deHiking = Subset(Country.de, NetworkType.hiking)
  val deBicycle = Subset(Country.de, NetworkType.bicycle)

  val all = Seq(
    nlBicycle,
    beBicycle,
    deBicycle,
    nlHiking,
    beHiking,
    deHiking,
    nlHorse,
    nlCanoe,
    nlMotorboat
  )

  val used: Seq[Subset] = Seq(
    nlBicycle,
    beBicycle,
    deBicycle,
    nlHiking,
    beHiking,
    nlHorse,
    nlCanoe,
    nlMotorboat
  )

  def of(domain: String, networkType: String): Option[Subset] = {
    all.find(s => s.country.domain == domain && s.networkType.name == networkType)
  }

  def of(country: Country, networkType: NetworkType): Option[Subset] = {
    all.find(s => s.country == country && s.networkType == networkType)
  }
}

case class Subset(country: Country, networkType: NetworkType) extends Ordered[Subset] {

  def key: String = country.domain + ":" + networkType.name

  def name: String = country.domain + "-" + networkType.name

  def string: String = country.domain + "/" + networkType.name

  def matches(countryDomain: String, networkTypeName: String): Boolean = {
    country.domain == countryDomain && networkType.name == networkTypeName
  }
  import scala.math.Ordered.orderingToOrdered
  def compare(that: Subset): Int = (this.country.domain, this.networkType.name).compare((that.country.domain, that.networkType.name))
}
