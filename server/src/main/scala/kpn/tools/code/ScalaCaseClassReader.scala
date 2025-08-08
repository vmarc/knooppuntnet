package kpn.tools.code

import kpn.core.util.Log
import kpn.tools.code.domain.ClassField
import kpn.tools.code.domain.ClassInfo
import kpn.tools.code.domain.ClassType

import scala.io.Source
import scala.util.matching.Regex

class ScalaCaseClassReader {

  private val log = Log(classOf[ScalaCaseClassReader])

  private val MapSignature: Regex = """Map\[([a-zA-Z0-9.]*),([a-zA-Z0-9.]*)\]""".r
  private val SetSignature: Regex = """Set\[([a-zA-Z0-9.]*)\]""".r
  private val SeqSignature: Regex = """Seq\[([a-zA-Z0-9.]*)\]""".r
  private val ArraySignature: Regex = """Array\[([a-zA-Z0-9.]*)\]""".r
  private val VectorSignature: Regex = """Vector\[([a-zA-Z0-9.]*)\]""".r
  private val OptionSignature: Regex = """^Option\[(.*)\]$""".r

  def read(classId: ClassId): ClassInfo = {

    Log.context(s"${classId.packageName}.${classId.className}") {

      val filename = s"src/main/scala/${classId.packageName.replaceAll("\\.", "/")}/${classId.className}.scala"

      val source = Source.fromFile(filename)

      try {
        val lines = source.getLines().toSeq.map(_.split("\\/\\/").head)

        var inObject = false
        val linesWithoutObject = lines.flatMap { line =>
          if (line.startsWith("object")) {
            inObject = true
            None
          }
          else if (line.startsWith("}")) {
            inObject = false
            None
          }
          else if (inObject) {
            None
          }
          else {
            Some(line)
          }
        }

        var inComment = false
        val linesWithoutComments = linesWithoutObject.flatMap { line =>
          if (line.trim.startsWith("/*")) {
            inComment = true
            None
          }
          else if (line.trim.endsWith("*/")) {
            inComment = false
            None
          }
          else if (inComment) {
            None
          }
          else {
            Some(line)
          }
        }

        val cleanedLines = linesWithoutComments.filter(_.nonEmpty)

        val importLines = cleanedLines.filter(_.startsWith("import "))

        val importMap = importLines.map { line =>
          val pattern = """import ([a-zA-Z0-9.]*)\.([a-zA-Z0-9]*)""".r
          pattern.findFirstMatchIn(line) match {
            case Some(m) => m.group(2) -> m.group(1)
            case None => throw new RuntimeException(s"cannot parse import line: $line in $filename")
          }
        }.toMap

        val packageName = cleanedLines
          .filter(_.startsWith("package"))
          .map(_.drop("package ".length))
          .headOption.getOrElse(throw new RuntimeException(s"cannot find package in $filename"))

        val classLines = cleanedLines.filterNot { line => line.startsWith("package") || line.startsWith("import") || line.startsWith("// ") }
        val content = classLines.mkString.takeWhile(_ != '{')

        val caseClassPattern = """case class (\w+)\((.*?)\)""".r
        val enumPattern = """sealed trait (\w+) extends EnumEntry""".r
        val fieldPattern = """\s*(\w+)\s*:\s*([\w\[\]\.]+)""".r

        caseClassPattern.findFirstMatchIn(content) match {
          case Some(m) =>
            val className = m.group(1)
            val fieldsString = m.group(2)
            val fields = fieldsString.split(",").map(_.split("=").head.trim).map {
              case fieldPattern(fieldName, fieldTypeString) =>
                ClassField(fieldName, buildClassType(classId, importMap, fieldTypeString))
              case _ =>
                throw new IllegalStateException(s"could not parse $fieldsString in $className")
            }.toSeq
            ClassInfo(className, packageName, fields)
          case None =>
            enumPattern.findFirstMatchIn(content) match {
              case Some(m) =>
                val className = m.group(1)
                ClassInfo(className, packageName, Seq.empty, isEnum = true)
              case None =>
                throw new IllegalStateException(s"could not parse case class $filename")
            }
        }
      } finally {
        source.close()
      }
    }
  }

  @scala.annotation.tailrec
  private def buildClassType(classId: ClassId, importMap: Map[String, String], fieldTypeString: String, optional: Boolean = false): ClassType = {

    fieldTypeString match {

      case MapSignature(type1, type2) =>
        val typescriptType1 = fieldTypeToTypescript(classId, importMap, type1, optional)
        val typescriptType2 = fieldTypeToTypescript(classId, importMap, type2, optional)
        ClassType(
          mapTypes = Some((typescriptType1, typescriptType2)),
          optional = optional
        )

      case SetSignature(type1) =>
        val typescriptType1 = fieldTypeToTypescript(classId, importMap, type1, optional)
        ClassType(
          arrayTypeClass = Some("Set"),
          arrayType = Some(typescriptType1),
          optional = optional
        )

      case SeqSignature(type1) =>
        val typescriptType1 = fieldTypeToTypescript(classId, importMap, type1, optional)
        ClassType(
          arrayTypeClass = Some("Seq"),
          arrayType = Some(typescriptType1),
          optional = optional
        )

      case VectorSignature(type1) =>
        val typescriptType1 = fieldTypeToTypescript(classId, importMap, type1, optional)
        ClassType(
          arrayTypeClass = Some("Vector"),
          arrayType = Some(typescriptType1),
          optional = optional
        )

      case ArraySignature(type1) =>
        val typescriptType1 = fieldTypeToTypescript(classId, importMap, type1, optional)
        ClassType(
          arrayTypeClass = Some("Array"),
          arrayType = Some(typescriptType1),
          optional = optional
        )

      case OptionSignature(type1) =>
        buildClassType(classId, importMap, type1, optional = true)

      case _ =>
        fieldTypeToTypescript(classId, importMap, fieldTypeString, optional)
    }
  }

  private def fieldTypeToTypescript(classId: ClassId, importMap: Map[String, String], fieldType: String, optional: Boolean): ClassType = {
    fieldType match {
      case "Int" => ClassType(Some("Int"), primitive = true, optional = optional)
      case "Long" => ClassType(Some("Long"), primitive = true, optional = optional)
      case "Double" => ClassType(Some("Double"), primitive = true, optional = optional)
      case "String" => ClassType(Some("String"), primitive = true, optional = optional)
      case "Boolean" => ClassType(Some("Boolean"), primitive = true, optional = optional)
      case _ =>
        val packageName = importMap.getOrElse(fieldType, classId.packageName)
        ClassType(Some(fieldType), packageName = Some(packageName), optional = optional)
    }
  }
}
