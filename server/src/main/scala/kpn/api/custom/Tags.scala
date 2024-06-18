package kpn.api.custom

object Tags {

  def from(tags: (String, String)*): Seq[Tag] = {
    tags.map(a => Tag(a._1, a._2))
  }

  def from(map: Map[String, String]): Seq[Tag] = {
    map.keys.toSeq.map(key => Tag(key, map(key)))
  }

  def toString(tags: Seq[Tag]): String = tags.map { case Tag(key, value) => s"$key=$value" }.mkString(", ")

  def get(tags: Seq[Tag], key: String): Option[String] = {
    tags.find(_.key == key).map(_.value)
  }

  def has(tags: Seq[Tag], key: String, allowedValues: String*): Boolean = {
    if (allowedValues.isEmpty) {
      tags.nonEmpty && tags.exists(_.key == key)
    }
    else {
      tags.exists { tag =>
        if (tag.key == key) {
          tag.value.split(";").exists(allowedValues.contains(_))
        }
        else {
          false
        }
      }
    }
  }
}
