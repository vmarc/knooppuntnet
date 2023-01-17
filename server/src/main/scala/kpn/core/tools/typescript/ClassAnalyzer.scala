package kpn.core.tools.typescript

import kpn.api.common.data.raw.RawNode

import scala.reflect.runtime.universe._
import scala.util.matching.Regex

object ClassAnalyzer {

  def main(args: Array[String]): Unit = {
    val mirror = runtimeMirror(classOf[RawNode].getClassLoader)
    val caseClass: Type = mirror.staticClass("kpn.api.shared.statistics.Statistics").typeSignature
    val classInfo = new ClassAnalyzer().analyze(caseClass)
    println(classInfo)
  }
}

class ClassAnalyzer {

  private val mapSignature: Regex = """Map\[([a-zA-Z0-9.]*),([a-zA-Z0-9.]*)\]""".r
  private val setSignature: Regex = """Set\[([a-zA-Z0-9.]*)\]""".r
  private val seqSignature: Regex = """Seq\[([a-zA-Z0-9.]*)\]""".r
  private val arraySignature: Regex = """Array\[([a-zA-Z0-9.]*)\]""".r
  private val vectorSignature: Regex = """Vector\[([a-zA-Z0-9.]*)\]""".r
  private val optionSignature: Regex = """^Option\[(.*)\]$""".r

  def analyze(caseClass: Type): ClassInfo = {

    val className = caseClass.typeSymbol.name.toString
    val packageName = caseClass.typeSymbol.fullName.dropRight(className.length + 1)
    val dirName = packageName.replaceAll("\\.", "/")
    val fileName = dirName + "/" + CamelCaseUtil.toDashed(className) + ".ts"

    val fields = caseClass.decls.toSeq.flatMap {
      case m: MethodSymbol if m.isCaseAccessor =>

        val fieldName = m.name.toString
        val fieldTypeString = m.typeSignature.toString
        val classType = buildClassType(fieldTypeString)
        Some(ClassField(fieldName, classType))

      case _ => None
    }

    val dependencies = caseClass.decls.toSeq.flatMap {
      case m: MethodSymbol if m.isCaseAccessor =>
        val fieldTypeString = m.typeSignature.toString
        val fieldClassNames = typescriptTypeDependencies(fieldTypeString)

        fieldClassNames.flatMap { fieldClassName =>

          val withoutPackage = if (fieldClassName.contains(".")) {
            // field type without package name
            fieldClassName.substring(fieldClassName.lastIndexOf(".") + 1)
          }
          else {
            fieldClassName
          }

          if (withoutPackage == className || withoutPackage.startsWith("Array")) {
            None
          }
          else {
            val fieldPackageName = if (fieldClassName.contains(".")) {
              // field type without package name
              fieldClassName.substring(0, fieldClassName.lastIndexOf("."))
            }
            else {
              ""
            }

            val fileName = {
              val fieldDirName = fieldPackageName.replaceAll("\\.", "/")
              RelativePathUtil.dependencyRelativePath(dirName, fieldDirName) + "/" + CamelCaseUtil.toDashed(withoutPackage)
            }
            Some(
              ClassDependency(
                withoutPackage,
                fileName
              )
            )
          }
        }

      case _ => Seq.empty
    }

    val formClass = TypescriptTool.formClasses.contains(className)

    ClassInfo(
      className,
      fileName,
      fields,
      dependencies.distinct.sortBy(_.className),
      formClass
    )
  }

  @scala.annotation.tailrec
  private def buildClassType(fieldTypeString: String, optional: Boolean = false): ClassType = {

    fieldTypeString match {

      case mapSignature(type1, type2) =>
        val typescriptType1 = fieldTypeToTypescript(type1)
        val typescriptType2 = fieldTypeToTypescript(type2)
        ClassType(
          s"Map<${typescriptType1.typeName}, ${typescriptType2.typeName}>",
          mapTypes = Some((typescriptType1, typescriptType2)),
          optional = optional
        )

      case setSignature(type1) =>
        val typescriptType1 = fieldTypeToTypescript(type1)
        ClassType(
          s"List<${typescriptType1.typeName}>",
          arrayType = Some(typescriptType1),
          optional = optional
        )

      case seqSignature(type1) =>
        val typescriptType1 = fieldTypeToTypescript(type1)
        ClassType(
          s"Array<${typescriptType1.typeName}>",
          arrayType = Some(typescriptType1),
          optional = optional
        )

      case vectorSignature(type1) =>
        val typescriptType1 = fieldTypeToTypescript(type1)
        ClassType(
          s"Array<${typescriptType1.typeName}>",
          arrayType = Some(typescriptType1),
          optional = optional
        )

      case arraySignature(type1) =>
        val typescriptType1 = fieldTypeToTypescript(type1)
        ClassType(
          s"Array<${typescriptType1.typeName}>",
          arrayType = Some(typescriptType1),
          optional = optional
        )

      case optionSignature(type1) =>
        buildClassType(type1, optional = true)

      case _ =>
        fieldTypeToTypescript(fieldTypeString)
    }
  }

  @scala.annotation.tailrec
  private def typescriptTypeDependencies(fieldTypeString: String): Seq[String] = {
    fieldTypeString match {
      case mapSignature(type1, type2) =>
        Seq(fieldTypeToDepencency(type1), fieldTypeToDepencency(type2)).flatten

      case setSignature(type1) =>
        Seq(fieldTypeToDepencency(type1)).flatten

      case seqSignature(type1) =>
        Seq(fieldTypeToDepencency(type1)).flatten

      case vectorSignature(type1) =>
        Seq(fieldTypeToDepencency(type1)).flatten

      case optionSignature(type1) =>
        typescriptTypeDependencies(type1)

      case _ =>
        Seq(fieldTypeToDepencency(fieldTypeString)).flatten
    }
  }

  private def fieldTypeToTypescript(fieldType: String): ClassType = {
    fieldType match {
      case "Int" => ClassType("number", primitive = true)
      case "Long" => ClassType("number", primitive = true)
      case "Double" => ClassType("number", primitive = true)
      case "String" => ClassType("string", primitive = true)
      case "Boolean" => ClassType("boolean", primitive = true)
      case _ =>
        val classType = if (fieldType.contains(".")) {
          // field type without package name
          fieldType.substring(fieldType.lastIndexOf(".") + 1)
        }
        else {
          fieldType
        }
        ClassType(classType)
    }
  }

  private def fieldTypeToDepencency(fieldType: String): Option[String] = {
    fieldType match {
      case "Int" => None
      case "Long" => None
      case "Double" => None
      case "String" => None
      case "Boolean" => None
      case _ => Some(fieldType)
    }
  }

}
