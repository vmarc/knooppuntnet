package kpn.core.tools.translations.domain

import java.io.PrintWriter
import java.io.StringWriter

class TranslationFileWriter(translationFile: TranslationFile) {

  private val sw = new StringWriter
  private val out = new PrintWriter(sw)

  out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
  out.println("<xliff version=\"1.2\" xmlns=\"urn:oasis:names:tc:xliff:document:1.2\">")
  out.println("  <file source-language=\"en\" datatype=\"plaintext\" original=\"ng2.template\">")
  out.println("    <body>")

  translationFile.translationUnits.foreach { translationUnit =>
    val state = translationUnit.state match {
      case Some(value) => s""" state="$value""""
      case None => ""
    }
    out.println(s"""      <trans-unit id="${translationUnit.id}" datatype="html">""")
    out.println(s"""        <source>${translationUnit.source}</source>""")
    out.println(s"""        <target$state>${translationUnit.target}</target>""")
    translationUnit.locations.foreach { location =>
      out.println("""        <context-group purpose="location">""")
      out.println(s"""          <context context-type="sourcefile">${location.sourceFile}</context>""")
      out.println(s"""          <context context-type="linenumber">${location.lineNumber}</context>""")
      out.println("        </context-group>")
    }
    out.println(s"""      </trans-unit>""")
  }

  out.println("    </body>")
  out.println("  </file>")
  out.println("</xliff>")
  out.close()

  val string: String = sw.toString
}
