package kpn.core.tools.typescript

import kpn.api.common.data.raw.RawNode

import scala.reflect.runtime.universe.*
import scala.util.matching.Regex

object OldClassAnalyzer {

  def main(args: Array[String]): Unit = {
    val mirror = runtimeMirror(classOf[RawNode].getClassLoader)
    val caseClass: Type = mirror.staticClass("kpn.api.shared.statistics.Statistics").typeSignature
    val classInfo = new OldClassAnalyzer().analyze(caseClass)
    println(classInfo)
  }
}

class OldClassAnalyzer {

  private val mapSignature: Regex = """Map\[([a-zA-Z0-9.]*),([a-zA-Z0-9.]*)\]""".r
  private val setSignature: Regex = """Set\[([a-zA-Z0-9.]*)\]""".r
  private val seqSignature: Regex = """Seq\[([a-zA-Z0-9.]*)\]""".r
  private val arraySignature: Regex = """Array\[([a-zA-Z0-9.]*)\]""".r
  private val vectorSignature: Regex = """Vector\[([a-zA-Z0-9.]*)\]""".r
  private val optionSignature: Regex = """^Option\[(.*)\]$""".r

  def analyze(caseClass: Type): OldClassInfo = {

    val className = caseClass.typeSymbol.name.toString
    val packageName = caseClass.typeSymbol.fullName.dropRight(className.length + 1)

    val fields = caseClass.decls.toSeq.flatMap {
      case m: MethodSymbol if m.isCaseAccessor =>

        val fieldName = m.name.toString
        val fieldTypeString = m.typeSignature.toString
        val classType = buildClassType(fieldTypeString)
        Some(OldClassField(fieldName, classType))

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
              if (fieldPackageName == packageName) {
                s"./${CamelCaseUtil.toDashed(withoutPackage)}"
              }
              else if (fieldPackageName.startsWith("kpn.api.")) {
                s"@api/${fieldPackageName.substring("kpn.api.".length).replaceAll("\\.", "/")}/${CamelCaseUtil.toDashed(withoutPackage)}"
              }
              else {
                throw new RuntimeException(
                  s"""unexpected field package name "$fieldPackageName" in find field "$fieldClassName" in class "$className"""")
              }
            }
            Some(
              OldClassDependency(
                withoutPackage,
                fileName
              )
            )
          }
        }

      case _ => Seq.empty
    }

    val formClass = TypescriptTool.formClasses.contains(className)

    val sortedDependencies = dependencies.distinct.sortWith { (a, b) =>
      if (a.fileName == b.fileName) {
        a.className < b.className
      }
      else {
        if (a.fileName.startsWith(".")) {
          if (b.fileName.startsWith(".")) {
            a.fileName < b.fileName
          }
          else {
            false
          }
        }
        else {
          if (b.fileName.startsWith(".")) {
            true
          }
          else {
            a.fileName < b.fileName
          }
        }
      }
    }

    OldClassInfo(
      className,
      fields,
      sortedDependencies,
      formClass
    )
  }

  @scala.annotation.tailrec
  private def buildClassType(fieldTypeString: String, optional: Boolean = false): OldClassType = {

    fieldTypeString match {

      case mapSignature(type1, type2) =>
        val typescriptType1 = fieldTypeToTypescript(type1, optional)
        val typescriptType2 = fieldTypeToTypescript(type2, optional)
        OldClassType(
          s"Map<${typescriptType1.typeName}, ${typescriptType2.typeName}>",
          mapTypes = Some((typescriptType1, typescriptType2)),
          optional = optional
        )

      case setSignature(type1) =>
        val typescriptType1 = fieldTypeToTypescript(type1, optional)
        OldClassType(
          s"List<${typescriptType1.typeName}>",
          arrayType = Some(typescriptType1),
          optional = optional
        )

      case seqSignature(type1) =>
        val typescriptType1 = fieldTypeToTypescript(type1, optional)
        OldClassType(
          s"Array<${typescriptType1.typeName}>",
          arrayType = Some(typescriptType1),
          optional = optional
        )

      case vectorSignature(type1) =>
        val typescriptType1 = fieldTypeToTypescript(type1, optional)
        OldClassType(
          s"Array<${typescriptType1.typeName}>",
          arrayType = Some(typescriptType1),
          optional = optional
        )

      case arraySignature(type1) =>
        val typescriptType1 = fieldTypeToTypescript(type1, optional)
        OldClassType(
          s"Array<${typescriptType1.typeName}>",
          arrayType = Some(typescriptType1),
          optional = optional
        )

      case optionSignature(type1) =>
        buildClassType(type1, optional = true)

      case _ =>
        fieldTypeToTypescript(fieldTypeString, optional)
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

  private def fieldTypeToTypescript(fieldType: String, optional: Boolean): OldClassType = {
    fieldType match {
      case "Int" => OldClassType("number", primitive = true)
      case "Long" => OldClassType("number", primitive = true)
      case "Double" => OldClassType("number", primitive = true)
      case "String" => OldClassType("string", primitive = true)
      case "Boolean" => OldClassType("boolean", primitive = true)
      case _ =>
        val classType = if (fieldType.contains(".")) {
          // field type without package name
          fieldType.substring(fieldType.lastIndexOf(".") + 1)
        }
        else {
          fieldType
        }
        OldClassType(classType, optional = optional)
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
