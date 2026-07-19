package kpn.core.taginfo

import kpn.core.doc.Storable

case class TagInfoTag(
  key: String,
  value: Option[String],
  description: String,
  object_types: Option[Seq[String]] = None
) extends Storable
