package kpn.core.tools.translations

import kpn.database.base.Options
import kpn.database.base.Tool
import kpn.server.json.Json

import java.io.File
import java.io.FileReader
import java.io.FileWriter

/*
  Postprocesses the translations file that is extracted from the Angular code
  to prepare for import in POEditor.
 */
object TranslationExtractTool extends Tool[TranslationExtractToolOptions] {

  override def options: Options[TranslationExtractToolOptions] = TranslationExtractToolOptions

  override def execute(options: TranslationExtractToolOptions): Unit = {
    new TranslationExtractTool(options.root).process()
  }
}

class TranslationExtractTool(root: String) {

  def process(): Unit = {
    val translations = loadSourceTranslations()
    val poeTranslations = sortAndTrimTranslations(translations.translations)
    saveProcessedTranslations(poeTranslations)
  }

  private def sortAndTrimTranslations(translations: Map[String, String]): PoeTranslations = {
    val trimmedTranslations = translations.map { case (key, value) => (key, value.trim) }
    PoeTranslations(trimmedTranslations)
  }

  private def loadSourceTranslations(): Translations = {
    val file = sourceTranslationsFile()
    val reader = new FileReader(file)
    try {
      val translations = Json.readValue(reader, classOf[Translations])
      translations
    }
    finally {
      reader.close()
    }
  }

  private def sourceTranslationsFile(): File = {
    val file = new File(s"$root/locale/translations-extract.json")
    if (!file.exists()) {
      throw new RuntimeException(s"extracted translations file not found: ${file.getAbsolutePath}")
    }
    file
  }

  private def saveProcessedTranslations(poeTranslations: PoeTranslations): Unit = {
    val file = new File(s"$root/locale/translations.json")
    val writer = new FileWriter(file)
    try {
      writer.write(Json.pretty(poeTranslations))
    }
    finally {
      writer.close()
    }
  }
}
