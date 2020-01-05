package kpn.shared

object Subset {

  val beHiking: Subset = Subset(Country.be, NetworkType.hiking)
  val beBicycle: Subset = Subset(Country.be, NetworkType.bicycle)
  val beHorseRiding: Subset = Subset(Country.be, NetworkType.horseRiding)

  val nlHiking: Subset = Subset(Country.nl, NetworkType.hiking)
  val nlBicycle: Subset = Subset(Country.nl, NetworkType.bicycle)
  val nlHorseRiding: Subset = Subset(Country.nl, NetworkType.horseRiding)
  val nlCanoe: Subset = Subset(Country.nl, NetworkType.canoe)
  val nlMotorboat: Subset = Subset(Country.nl, NetworkType.motorboat)
  val nlInlineSkates: Subset = Subset(Country.nl, NetworkType.inlineSkates)

  val deHiking: Subset = Subset(Country.de, NetworkType.hiking)
  val deBicycle: Subset = Subset(Country.de, NetworkType.bicycle)
  val deHorseRiding: Subset = Subset(Country.de, NetworkType.horseRiding)

  val frHiking: Subset = Subset(Country.fr, NetworkType.hiking)
  val frBicycle: Subset = Subset(Country.fr, NetworkType.bicycle)
  val frHorseRiding: Subset = Subset(Country.fr, NetworkType.horseRiding)

  val atBicycle: Subset = Subset(Country.at, NetworkType.bicycle)

  val all: Seq[Subset] = Seq(
    nlBicycle,
    beBicycle,
    deBicycle,
    frBicycle,
    atBicycle,
    nlHiking,
    beHiking,
    deHiking,
    frHiking,
    nlHorseRiding,
    beHorseRiding,
    deHorseRiding,
    frHorseRiding,
    nlCanoe,
    nlMotorboat,
    nlInlineSkates
  )

  def of(domain: String, networkType: String): Option[Subset] = {
    all.find(s => s.country.domain == domain && s.networkType.name == networkType)
  }

  def ofNewName(domain: String, networkTypeNewName: String): Option[Subset] = {
    all.find(s => s.country.domain == domain && s.networkType.newName == networkTypeNewName)
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
