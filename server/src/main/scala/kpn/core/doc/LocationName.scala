package kpn.core.doc

import kpn.api.common.Language
import kpn.api.custom.Tag
import kpn.api.custom.Tags

object LocationNames {

  def from(tags: Seq[Tag], name: String): Seq[LocationName] = {
    Language.values.flatMap { language =>
      val lang = language.toString.toLowerCase
      Tags.values(tags, s"name:$lang").flatMap { value =>
        Option.when(value != name) {
          LocationName(
            language,
            value
          )
        }
      }
    }
  }
}

case class LocationName(
  language: Language,
  name: String
)
