package kpn.core.tools.translations.domain

case class TranslationFile(
  sourceLanguage: String,
  targetLanguage: String,
  translationUnits: Seq[TranslationUnit]
)
