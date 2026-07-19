package kpn.tools.code

import kpn.api.common.Fact
import kpn.tools.code.domain.ClassInfo
import kpn.tools.code.typescript.CamelCaseUtil

import java.io.PrintStream

class JavaEnumerationWriter(out: PrintStream, classInfo: ClassInfo) {

  def write(): Unit = {
    writeHeader()
    writeEnumType()
    writeEnumValues()
  }

  private def writeHeader(): Unit = {
    out.println(s"package ${classInfo.packageName};\n")
  }

  private def writeEnumType(): Unit = {
    out.println(s"public enum ${classInfo.className} {")
  }

  private def writeEnumValues(): Unit = {
    val enumValues = formattedEnumValues().sorted
    enumValues.zipWithIndex.foreach { case (value, index) =>
      val lineEnd = if (index < enumValues.length - 1) "," else ""
      out.println(s"  $value$lineEnd")
    }
    out.println("}\n")
  }

  private def formattedEnumValues(): Seq[String] = {
    classInfo.enumValues.map { value =>
      if (classInfo.className != classOf[Fact].getSimpleName) {
        CamelCaseUtil.toEnumValue(value)
      } else {
        value
      }
    }
  }
}
