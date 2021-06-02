package kpn.core.tools.typescript

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
    classInfo.dependencies.map { dependency =>
      if (dependency.className == "PlanCoordinate") {
        "import { Coordinate } from 'ol/coordinate';"
      }
      else {
        s"import { ${dependency.className} } from '${dependency.fileName}';"
      }
    }
  }

  private def writeContents(): Unit = {
    out.println(s"export interface ${classInfo.className} {")
    val fields = classInfo.fields.map { field =>
      val fieldType = {
        val typeName = field.classType.typeName
        if (typeName.startsWith("List<")) {
          val arrayTypeName = typeName.drop("List<".length).dropRight(1) + "[]"
          s"${field.name}: $arrayTypeName"
        }
        else if (typeName.startsWith("Array<")) {
          val arrayTypeName = typeName.drop("Array<".length).dropRight(1) + "[]"
          s"${field.name}: $arrayTypeName"
        }
        else if (typeName == "PlanCoordinate") {
          s"${field.name}: Coordinate"
        }
        else {
          s"${field.name}: $typeName"
        }
      }
      fieldType + (if (field.classType.optional) " | undefined" else "")
    }
    fields.mkString("  readonly ", ";\n  readonly ", ";\n").foreach {
      out.print
    }
    out.println("}")
  }

}
