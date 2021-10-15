package kpn.core.taginfo

case class TagInfoTag(
  key: String,
  value: Option[String],
  description: String,
  object_types: Option[Seq[String]] = None
)
