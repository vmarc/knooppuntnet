package kpn.core.tools.translations

import kpn.core.tools.translations.domain.TranslationFileReader
import kpn.core.tools.translations.domain.Trim

object TranslationReportTool {
  def main(args: Array[String]): Unit = {
    TranslationReportToolOptions.parse(args) foreach { options =>
      new TranslationReportTool(options.filename).translations()
    }
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
