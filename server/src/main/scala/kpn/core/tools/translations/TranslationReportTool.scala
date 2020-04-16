package kpn.core.tools.translations

import java.io.File
import java.io.PrintWriter

import kpn.core.common.Time
import kpn.core.tools.translations.domain.TranslationFile
import kpn.core.tools.translations.domain.TranslationFileReader
import kpn.core.tools.translations.domain.Trim

object TranslationReportTool {
  def main(args: Array[String]): Unit = {
    TranslationReportToolOptions.parse(args) foreach { options =>
      new TranslationReportTool(options.root).report()
    }
  }
}

class TranslationReportTool(root: String) {

  def report(): Unit = {
    Seq("nl", "de", "fr").foreach(reportLanguage)
  }

  private def reportLanguage(language: String): Unit = {
    val filename = s"$root/locale/translations.$language.xlf"
    val translationFile = new TranslationFileReader().read(filename)
    reportMissingTranslations(language, translationFile)
    reportTranslations(language, translationFile)
  }

  private def reportMissingTranslations(language: String, translationFile: TranslationFile): Unit = {
    val filename = s"$root/assets/translations/missing.$language.txt"
    val out = new PrintWriter(new File(filename))
    val missingTranslations = translationFile.translationUnits.filter(_.state.contains("new"))
    out.println(s"# Missing '$language' translations: ${missingTranslations.size} of ${translationFile.translationUnits.size}")
    // note that we retain the order of the translation units in the source file, to help when manual editing targets
    missingTranslations.foreach { translationUnit =>
      out.println(s"[${translationUnit.id.toUpperCase}] ${Trim.trim(translationUnit.source)}")
    }
    out.close()
  }

  private def reportTranslations(language: String, translationFile: TranslationFile): Unit = {
    val filename = s"$root/assets/translations/$language.tsv"
    val out = new PrintWriter(new File(filename))
    out.println(s"# Translations: ${translationFile.translationUnits.size}")
    out.println(s"Nr\tTranslated\tId\tTranslation\tEnglish")
    translationFile.translationUnits.sortBy(_.id).zipWithIndex.foreach { case (translationUnit, index) =>
      val translated = if (translationUnit.state.contains("new")) "N" else "Y"
      val id = translationUnit.id
      val source = Trim.trim(translationUnit.source)
      val target = Trim.trim(translationUnit.target)
      out.println(s"${index + 1}\t$translated\t$id\t$target\t$source")
    }
    out.close()
  }

}
