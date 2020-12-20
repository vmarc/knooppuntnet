package kpn.server.api.monitor.domain

import kpn.api.common.DE
import kpn.api.common.EN
import kpn.api.common.FR
import kpn.api.common.Language
import kpn.api.common.NL

case class MonitorRoute(
  id: Long,
  groupName: String,
  name: String,
  nameNl: Option[String],
  nameEn: Option[String],
  nameDe: Option[String],
  nameFr: Option[String],
  ref: Option[String],
  description: Option[String],
  operator: Option[String],
  website: Option[String]
) {

  def translatedName(language: Language): String = {
    language match {
      case NL => nameNl.getOrElse(name)
      case EN => nameEn.getOrElse(name)
      case FR => nameFr.getOrElse(name)
      case DE => nameDe.getOrElse(name)
      case _ => name
    }
  }

}
