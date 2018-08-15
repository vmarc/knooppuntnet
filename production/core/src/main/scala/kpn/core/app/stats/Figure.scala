package kpn.core.app.stats

import kpn.core.util.Formatter.number
import kpn.shared.Country
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.statistics.CountryStatistic
import kpn.shared.statistics.Statistic

case class Figure(
  name: String,
  nlRwn: Int,
  nlRcn: Int,
  nlRhn: Int,
  nlRmn: Int,
  nlRpn: Int,
  beRwn: Int,
  beRcn: Int,
  beRhn: Int,
  beRmn: Int,
  beRpn: Int,
  deRwn: Int,
  deRcn: Int,
  deRhn: Int,
  deRmn: Int,
  deRpn: Int
) {

  def total: Int = nlRwn + nlRcn + nlRhn + nlRmn + nlRpn +
    beRwn + beRcn + beRhn + beRmn + beRpn +
    deRwn + deRcn + deRhn + deRmn + deRpn

  def value(subset: Subset): Int = {
    subset.country match {
      case Country.nl =>
        subset.networkType match {
          case NetworkType.hiking => nlRwn
          case NetworkType.bicycle => nlRcn
          case NetworkType.horse => nlRhn
          case NetworkType.motorboat => nlRmn
          case NetworkType.canoe => nlRpn
        }
      case Country.be =>
        subset.networkType match {
          case NetworkType.hiking => beRwn
          case NetworkType.bicycle => beRcn
          case NetworkType.horse => beRhn
          case NetworkType.motorboat => beRmn
          case NetworkType.canoe => beRpn
        }
      case Country.de =>
        subset.networkType match {
          case NetworkType.hiking => deRwn
          case NetworkType.bicycle => deRcn
          case NetworkType.horse => deRhn
          case NetworkType.motorboat => deRmn
          case NetworkType.canoe => deRpn
        }
    }
  }

  def toStatistic: Statistic = {
    Statistic(
      number(total),
      CountryStatistic(number(nlRwn), number(nlRcn), number(nlRhn), number(nlRmn), number(nlRpn)),
      CountryStatistic(number(beRwn), number(beRcn), number(beRhn), number(beRmn), number(beRpn)),
      CountryStatistic(number(deRwn), number(deRcn), number(deRhn), number(deRmn), number(deRpn))
    )
  }
}
