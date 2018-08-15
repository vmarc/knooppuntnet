package kpn.core.tools.typescript

import kpn.shared.data.raw.RawNode

import scala.reflect.runtime.universe._
import scala.util.matching.Regex

object ClassAnalyzer {

  def main(args: Array[String]): Unit = {
    val mirror = runtimeMirror(classOf[RawNode].getClassLoader)
    val caseClass: Type = mirror.staticClass("kpn.shared.statistics.Statistics").typeSignature
    val classInfo = new ClassAnalyzer().analyze(caseClass)
    println(classInfo)
  }
}

class ClassAnalyzer {

  private val mapSignature: Regex = """Map\[([a-zA-Z0-9.]*),([a-zA-Z0-9.]*)\]""".r
  private val setSignature: Regex = """Set\[([a-zA-Z0-9.]*)\]""".r
  private val seqSignature: Regex = """Seq\[([a-zA-Z0-9.]*)\]""".r
  private val optionSignature: Regex = """^Option\[(.*)\]$""".r

  def analyze(caseClass: Type): ClassInfo = {

    val className = caseClass.typeSymbol.name.toString
    val packageName = caseClass.typeSymbol.fullName.toString.dropRight(className.length + 1)
    val dirName = packageName.replaceAll("\\.", "/")
    val fileName = dirName + "/" + CamelCaseUtil.toDashed(className) + ".ts"

    val fields = caseClass.decls.toSeq.flatMap {
      case m: MethodSymbol if m.isCaseAccessor =>

        val fieldName = m.name.toString
        val fieldTypeString = m.typeSignature.toString.drop(3)
        val collection = isCollection(fieldTypeString)
        val classType = buildClassType(fieldTypeString)
        Some(ClassField(fieldName, classType))

      case _ => None
    }

    val dependencies = caseClass.decls.toSeq.flatMap {
      case m: MethodSymbol if m.isCaseAccessor =>
        val fieldName = m.name.toString
        val fieldTypeString = m.typeSignature.toString.drop(3)
        val typescriptFieldType = buildClassType(fieldTypeString)
        val fieldClassNames = typescriptTypeDependencies(fieldTypeString)

        fieldClassNames.flatMap { fieldClassName =>

          val withoutPackage = if (fieldClassName.contains(".")) {
            // field type without package name
            fieldClassName.substring(fieldClassName.lastIndexOf(".") + 1)
          }
          else {
            fieldClassName
          }

          if (withoutPackage == className) {
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

      case _ => Seq()
    }

    ClassInfo(
      className,
      fileName,
      fields,
      dependencies.distinct.sortBy(_.className)
    )
  }

  private def buildClassType(fieldTypeString: String): ClassType = {

    fieldTypeString match {

      case mapSignature(type1, type2) =>
        val typescriptType1 = fieldTypeToTypescript(type1)
        val typescriptType2 = fieldTypeToTypescript(type2)
        ClassType(
          s"Map<${typescriptType1.typeName}, ${typescriptType2.typeName}>",
          mapTypes = Some((typescriptType1, typescriptType2))
        )

      case setSignature(type1) =>
        val typescriptType1 = fieldTypeToTypescript(type1)
        ClassType(
          s"Array<${typescriptType1.typeName}>",
          arrayType = Some(typescriptType1)
        )

      case seqSignature(type1) =>
        val typescriptType1 = fieldTypeToTypescript(type1)
        ClassType(
          s"Array<${typescriptType1.typeName}>",
          arrayType = Some(typescriptType1)
        )

      case optionSignature(type1) =>
        buildClassType(type1)

      case _ =>
        fieldTypeToTypescript(fieldTypeString)
    }
  }

  private def typescriptTypeDependencies(fieldTypeString: String): Seq[String] = {
    fieldTypeString match {
      case mapSignature(type1, type2) =>
        Seq(fieldTypeToDepencency(type1), fieldTypeToDepencency(type2)).flatten

      case setSignature(type1) =>
        Seq(fieldTypeToDepencency(type1)).flatten

      case seqSignature(type1) =>
        Seq(fieldTypeToDepencency(type1)).flatten

      case optionSignature(type1) =>
        typescriptTypeDependencies(type1)

      case _ =>
        Seq(fieldTypeToDepencency(fieldTypeString)).flatten
    }
  }


  private def isCollection(fieldTypeString: String): Boolean = {
    Seq("Seq[", "Set[").exists(c => fieldTypeString.startsWith(c))
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
