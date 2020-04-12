package kpn.core.tools.translations.domain

object TranslationFileTrimmer {

  def trim(translationFile: TranslationFile): TranslationFile = {
    translationFile.copy(
      translationUnits = translationFile.translationUnits.map { translationUnit =>
        translationUnit.copy(
          source = Trim.trim(translationUnit.source),
          target = Trim.trim(translationUnit.target)
        )
      }
    )
  }

}
