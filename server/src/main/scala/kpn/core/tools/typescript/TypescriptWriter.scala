package kpn.core.tools.typescript

import kpn.tools.code.ClassId
import kpn.tools.code.domain.ClassInfo

import java.io.PrintStream

class TypescriptWriter(out: PrintStream, classInfo: ClassInfo) {

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

    val dependencies = classInfo.dependencies.filterNot(_.className == classInfo.className).map { dependency =>
      val className = dependency.className
      val typescriptPackage = if (classInfo.packageName == dependency.packageName) {
        "."
      }
      else {
        val packageName = s"${dependency.packageName.drop("kpn.api".length + 1).replaceAll("\\.", "/")}"
        if (packageName.isEmpty) {
          s"@api"
        }
        else {
          s"@api/$packageName"
        }
      }
      ClassId(className, typescriptPackage)
    }

    val sorted = dependencies.sortWith { (a, b) =>
      if (a.packageName == b.packageName) {
        a.className < b.className
      }
      else {
        if (a.packageName == ".") {
          false
        }
        else if (b.packageName == ".") {
          true
        }
        else {
          a.packageName < b.packageName
        }
      }
    }

    sorted.map { dependency =>
      if (dependency.className == "PlanCoordinate") {
        "import { Coordinate } from 'ol/coordinate';"
      }
      else {
        s"import { ${dependency.className} } from '${dependency.packageName}/${CamelCaseUtil.toDashed(dependency.className)}';"
      }
    }
  }

  private def writeContents(): Unit = {
    out.println(s"export interface ${classInfo.className} {")

    val fields = classInfo.fields.map { field =>
      val fieldName = if ( /* TODO classInfo.formClass || */ field.classType.optional) {
        s"${field.name}?"
      }
      else {
        field.name
      }

      field.classType.typeName match {
        case Some(typeName) =>
          if (typeName == "String") {
            s"$fieldName: string"
          }
          else if (typeName == "Boolean") {
            s"$fieldName: boolean"
          }
          else if (typeName == "Long") {
            s"$fieldName: number"
          }
          else if (typeName == "Int") {
            s"$fieldName: number"
          }
          else if (typeName == "Double") {
            s"$fieldName: number"
          }
          else if (typeName == "PlanCoordinate") {
            s"$fieldName: Coordinate"
          }
          else {
            s"$fieldName: $typeName"
          }

        case None =>
          field.classType.arrayType match {
            case Some(arrayType) =>
              arrayType.typeName match {
                case Some(typeName) =>
                  if (typeName == "String") {
                    s"$fieldName: string[]"
                  }
                  else if (typeName == "Boolean") {
                    s"$fieldName: boolean[]"
                  }
                  else if (typeName == "Long") {
                    s"$fieldName: number[]"
                  }
                  else if (typeName == "Int") {
                    s"$fieldName: number[]"
                  }
                  else if (typeName == "Double") {
                    s"$fieldName: number[]"
                  }
                  else if (typeName == "PlanCoordinate") {
                    s"$fieldName: Coordinate[]"
                  }
                  else {
                    s"$fieldName: $typeName[]"
                  }
                case None =>
                  ???
              }
            case None =>
              ???
          }
      }
    }
    fields.mkString("  readonly ", ";\n  readonly ", ";\n").foreach {
      out.print
    }
    out.println("}")
  }
}
