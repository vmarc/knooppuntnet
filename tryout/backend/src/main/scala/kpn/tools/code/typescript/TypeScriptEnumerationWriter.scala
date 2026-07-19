package kpn.tools.code.typescript

import kpn.api.common.Fact
import kpn.tools.code.domain.ClassInfo

import java.io.PrintStream

class TypeScriptEnumerationWriter(out: PrintStream, classInfo: ClassInfo) {

  def write(): Unit = {
    writeHeader()
    writeEnumType()
    writeEnumValues()
  }

  private def writeHeader(): Unit = {
    out.println("// this file is generated, please do not modify")
    out.println()
  }

  private def writeEnumType(): Unit = {
    out.println(s"export type ${classInfo.className} =")
  }

  private def writeEnumValues(): Unit = {
    val enumValues = formattedEnumValues().sorted
    enumValues.zipWithIndex.foreach { case (value, index) =>
      val lineEnd = if (index == enumValues.length - 1) ";" else ""
      out.println(s"  | '$value'$lineEnd")
    }
  }

  private def formattedEnumValues(): Seq[String] = {
    classInfo.enumValues.map { value =>
      if (classInfo.className != classOf[Fact].getSimpleName) {
        CamelCaseUtil.toDashed(value)
      } else {
        value
      }
    }
  }
}
