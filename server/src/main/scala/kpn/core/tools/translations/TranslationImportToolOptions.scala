package kpn.core.tools.translations

import kpn.database.base.Options

object TranslationImportToolOptions extends Options[TranslationImportToolOptions] {

  def parse(args: Array[String]): Option[TranslationImportToolOptions] = {
    optionParser.parse(args, TranslationImportToolOptions())
  }

  private def optionParser: scopt.OptionParser[TranslationImportToolOptions] = {
    new scopt.OptionParser[TranslationImportToolOptions]("TranslationImportToolOptions") {
      opt[String]('r', "root").required() valueName "<directory>" action { (x, c) =>
        c.copy(root = x)
      } text "frontend source root directory name"
    }
  }
}

case class TranslationImportToolOptions(
  root: String = ""
)
