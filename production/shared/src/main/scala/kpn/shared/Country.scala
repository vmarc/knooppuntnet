package kpn.shared

object Country {

  def withDomain(domain: String): Option[Country] = {
    all.find(_.domain == domain)
  }

  val nl = Country("nl")
  val be = Country("be")
  val de = Country("de")
  val all = Seq(nl, be, de)
}

case class Country(domain: String)
