package kpn.core.doc

import kpn.api.common.Language
import kpn.api.common.Languages
import kpn.api.custom.Tags

object LocationNames {

  def from(tags: Tags, name: String): Seq[LocationName] = {
    Languages.all.flatMap { language =>
      val lang = language.toString.toLowerCase
      tags(s"name:$lang") match {
        case None => None
        case Some(value) =>
          if (value != name) {
            Some(
              LocationName(
                language,
                value
              )
            )
          }
          else {
            None
          }
      }
    }
  }
}

case class LocationName(
  language: Language,
  name: String
)
