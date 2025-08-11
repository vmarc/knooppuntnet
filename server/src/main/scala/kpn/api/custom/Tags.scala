package kpn.api.custom

object Tags {
  private val ValueSeparator = ";"
  type TagTuple = (String, String)

  def from(tags: TagTuple*): Seq[Tag] = {
    tags.map { case (key, value) => Tag(key, value) }
  }

  def from(map: Map[String, String]): Seq[Tag] = {
    map.toSeq.map { case (key, value) => Tag(key, value) }
  }

  def toString(tags: Seq[Tag]): String = tags.map { case Tag(key, value) => s"$key=$value" }.mkString(", ")

  def get(tags: Seq[Tag], key: String): Option[String] = {
    tags.find(_.key == key).map(_.value)
  }

  def values(tags: Seq[Tag], key: String): Seq[String] = {
    get(tags, key)
      .filter(_.nonEmpty)
      .map(splitAndNormalize)
      .getOrElse(Seq.empty)
  }

  def has(tags: Seq[Tag], key: String, allowedValues: String*): Boolean = {
    tags.exists { tag =>
      tag.key == key && (
        allowedValues.isEmpty ||
          splitAndNormalize(tag.value).exists(allowedValues.contains)
        )
    }
  }

  def splitAndNormalize(value: String): Seq[String] = {
    if (value.contains(ValueSeparator)) {
      value.split(ValueSeparator)
        .toSeq
        .map(_.trim)
        .filter(_.nonEmpty)
        .sorted
    } else {
      Seq(value.trim)
    }
  }
}
