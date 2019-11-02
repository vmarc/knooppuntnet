package kpn.shared.data

import kpn.shared.NetworkType

object Tags {

  val empty: Tags = Tags(Seq())

  def from(tags: (String, String)*): Tags = {
    Tags(tags.map(a => Tag(a._1, a._2)))
  }

  def toString(tags: Seq[Tag]): String = tags.map { case Tag(key, value) => s"$key=$value" }.mkString(", ")
}

case class Tags(tags: Seq[Tag]) {

  def keys: Seq[String] = tags.map(_.key)

  def isEmpty: Boolean = tags.isEmpty

  def nonEmpty: Boolean = tags.nonEmpty

  def apply(key: String): Option[String] = {
    tags.find(_.key == key).map(_.value)
  }

  def has(key: String, allowedValues: String*): Boolean = {
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

  def without(key: String): Tags = {
    Tags(tags.filterNot(s => s.key == key))
  }

  def tagString: String = Tags.toString(tags)

  def ++(other: Tags): Tags = {
    Tags(tags ++ other.tags)
  }

  override def toString: String = {
    s"${getClass.getSimpleName}($tagString)"
  }

  def hasNodeTag: Boolean = hasHikingNodeTag || hasBicyleNodeTag

  def hasBicyleNodeTag: Boolean = has(NetworkType.bicycle.nodeTagKey)

  def hasHikingNodeTag: Boolean = has(NetworkType.hiking.nodeTagKey)

}
