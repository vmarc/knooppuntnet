package kpn.core.tools.translations

import kpn.core.tools.translations.domain.TranslationFileReader
import kpn.core.tools.translations.domain.Trim
import kpn.database.base.Options
import kpn.database.base.Tool

object TranslationReportTool extends Tool[TranslationReportToolOptions] {

  override def options: Options[TranslationReportToolOptions] = TranslationReportToolOptions

  override def execute(options: TranslationReportToolOptions): Unit = {
    new TranslationReportTool(options.filename).translations()
  }
}

class TranslationReportTool(filename: String) {

  def translations(): Unit = {
    val translationFile = new TranslationFileReader().read(filename)
    val translations = translationFile.translationUnits.sortBy(_.id)
    translations.foreach { translationUnit =>
      println(s"[${translationUnit.id}] ${Trim.trim(translationUnit.target)}")
    }
  }
}
