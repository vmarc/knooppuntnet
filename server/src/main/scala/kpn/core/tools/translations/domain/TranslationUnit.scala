package kpn.core.tools.translations.domain

case class TranslationUnit(
  id: String,
  source: String,
  target: String,
  state: Option[String],
  locations: Seq[TranslationLocation]
)
