package kpn.tools.code.typescript

import kpn.tools.code.ClassId
import kpn.tools.code.domain.ClassField
import kpn.tools.code.domain.ClassInfo

import java.io.PrintStream

class TypescriptClassWriter(out: PrintStream, classInfo: ClassInfo) {

  def write(): Unit = {
    writeComment()
    writeImports()
    writeContents()
  }

  private def writeComment(): Unit = {
    out.println("// this file is generated, please do not modify")
    out.println()
  }

  private def writeImports(): Unit = {
    val imports = dependencyImports()
    if (imports.nonEmpty) {
      out.println(imports.mkString("", "\n", "\n"))
    }
  }

  private def dependencyImports(): Seq[String] = {
    val dependencies = classInfo.dependencies
      .filterNot(_.className == classInfo.className)
      .map(createDependencyClassId)

    val sortedDependencies = sortDependencies(dependencies)

    sortedDependencies.map(formatImportStatement)
  }

  private def createDependencyClassId(dependency: ClassId): ClassId = {
    val className = dependency.className
    val typescriptPackage = if (classInfo.packageName == dependency.packageName) {
      "."
    } else {
      val packageName = s"${dependency.packageName.drop("kpn.api".length + 1).replaceAll("\\.", "/")}"
      if (packageName.isEmpty) {
        s"@api"
      } else {
        s"@api/$packageName"
      }
    }
    ClassId(className, typescriptPackage)
  }

  private def sortDependencies(dependencies: Seq[ClassId]): Seq[ClassId] = {
    dependencies.sortWith { (a, b) =>
      if (a.packageName == b.packageName) {
        a.className < b.className
      } else {
        if (a.packageName == ".") false
        else if (b.packageName == ".") true
        else a.packageName < b.packageName
      }
    }
  }

  private def formatImportStatement(dependency: ClassId): String = {
    if (dependency.className == "PlanCoordinate") {
      "import { Coordinate } from '@api/custom/coordinate';"
    } else {
      s"import { ${dependency.className} } from '${dependency.packageName}/${CamelCaseUtil.toDashed(dependency.className)}';"
    }
  }

  private def writeContents(): Unit = {
    out.println(s"export interface ${classInfo.className} {")
    val fieldDefinitions = classInfo.fields.map(formatField)
    fieldDefinitions.mkString("  readonly ", ";\n  readonly ", ";\n").foreach(out.print)
    out.println("}")
  }

  private def formatField(field: ClassField): String = {
    val fieldName = if (field.classType.optional) s"${field.name}?" else field.name

    field.classType.typeName match {
      case Some(typeName) =>
        s"$fieldName: ${mapScalaToTypeScriptType(typeName)}"
      case None =>
        field.classType.arrayType match {
          case Some(arrayType) =>
            arrayType.typeName match {
              case Some(typeName) =>
                s"$fieldName: ${mapScalaToTypeScriptType(typeName)}[]"
              case None =>
                throw new IllegalArgumentException(s"Missing type name for array type in field ${field.name}")
            }
          case None =>
            throw new IllegalArgumentException(s"Missing type information for field ${field.name}")
        }
    }
  }

  private def mapScalaToTypeScriptType(typeName: String): String = typeName match {
    case "String" => "string"
    case "Boolean" => "boolean"
    case "Long" | "Int" | "Double" => "number"
    case "PlanCoordinate" => "Coordinate"
    case _ => typeName
  }
}
