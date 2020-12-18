package kpn.core.tools.typescript

import java.io.PrintStream

object TypescriptWriter {

  def main(args: Array[String]): Unit = {

    val dependecies = Seq(
      ClassDependency("Fact", "../fact"),
      ClassDependency("ChangesPage", "../../changes/detail/changes-page")
    )

    val fields = Seq(
      ClassField("id", ClassType("number", primitive = true)),
      ClassField("version", ClassType("string", primitive = true)),
      ClassField("timestamp", ClassType("Timestamp")),
      ClassField("relation", ClassType("Array<RawRelation>", arrayType = Some(ClassType("RawRelation"))))
    )

    val classInfo = ClassInfo("RouteInfo", "", fields, dependecies)
    new TypescriptWriter(System.out, classInfo).write()
  }

}

class TypescriptWriter(out: PrintStream, classInfo: ClassInfo) {

  def write(): Unit = {
    writeComment()
    writeImports()
    writeClass()
  }

  def writeInterface(): Unit = {
    writeFileComment()
    writeInteraceImports()
    writeInterfaceContents()
  }

  private def writeComment(): Unit = {
    out.println("// this class is generated, please do not modify")
    out.println()
  }

  private def writeFileComment(): Unit = {
    out.println("// this file is generated, please do not modify")
    out.println()
  }

  private def writeImports(): Unit = {
    val imports = listImports() ++ mapImports() ++ dependencyImports()
    if (imports.nonEmpty) {
      out.println(imports.mkString("", "\n", "\n"))
    }
  }

  private def writeInteraceImports(): Unit = {
    val imports = dependencyImports()
    if (imports.nonEmpty) {
      out.println(imports.mkString("", "\n", "\n"))
    }
  }

  private def listImports(): Seq[String] = {
    if (classInfo.fields.exists(field => field.classType.isArray && !field.classType.typeName.contains("Array"))) {
      Seq("import {List} from 'immutable';")
    }
    else {
      Seq()
    }
  }

  private def mapImports(): Seq[String] = {
    if (classInfo.fields.exists(_.classType.isMap)) {
      Seq("import {Map} from 'immutable';")
    }
    else {
      Seq()
    }
  }

  private def dependencyImports(): Seq[String] = {
    classInfo.dependencies.map { dependency =>
      if (dependency.className == "PlanCoordinate") {
        "import {Coordinate} from 'ol/coordinate';"
      }
      else {
        s"import {${dependency.className}} from '${dependency.fileName}';"
      }
    }
  }

  private def writeClass(): Unit = {
    out.println(s"export class ${classInfo.className} {")
    out.println()
    writeConstructor()
    writeDeserializer()
    out.println("}")
  }

  private def writeConstructor(): Unit = {
    val fields = classInfo.fields.map { field =>
      val typeName = field.classType.typeName
      if (typeName == "PlanCoordinate") {
        s"${field.name}: Coordinate"
      }
      else {
        s"${field.name}: $typeName"
      }
    }
    out.print(s"  constructor(")
    if (fields.isEmpty) {
      out.println(") {")
    }
    else {
      fields.mkString("readonly ", ",\n              readonly ", ") {\n").foreach {
        out.print
      }
    }

    out.println("  }\n")
  }

  private def writeDeserializer(): Unit = {
    out.println(s"  public static fromJSON(jsonObject: any): ${classInfo.className} {")
    out.println(s"    if (!jsonObject) {")
    out.println(s"      return undefined;")
    out.println(s"    }")
    out.println(s"    return new ${classInfo.className}(")

    val fields = classInfo.fields.map { field =>
      if (field.classType.primitive) {
        s"      jsonObject.${field.name}"
      }
      else if (field.classType.typeName == "PlanCoordinate") {
        s"      [jsonObject.${field.name}.x, jsonObject.${field.name}.y]"
      }
      else {
        field.classType.arrayType match {
          case Some(arrayClassType) =>
            val collection = if (field.classType.typeName.startsWith("Array")) "Array" else "List"
            if (arrayClassType.primitive) {
              s"      jsonObject.${field.name} ? $collection(jsonObject.${field.name}) : $collection()"
            }
            else {
              s"      jsonObject.${field.name} ? $collection(jsonObject.${field.name}.map((json: any) => ${arrayClassType.typeName}.fromJSON(json))) : $collection()"
            }
          case _ =>
            s"      ${field.classType.typeName}.fromJSON(jsonObject.${field.name})"
        }
      }
    }

    out.print(fields.mkString("", ",\n", "\n"))

    out.println("    );")
    out.println("  }")
  }

  private def writeInterfaceContents(): Unit = {
    out.println(s"export interface ${classInfo.className} {")
    val fields = classInfo.fields.map { field =>
      val typeName = field.classType.typeName
      if (typeName.startsWith("List<")) {
        val arrayTypeName = typeName.drop("List<".length).dropRight(1) + "[]";
        s"${field.name}: $arrayTypeName"
      }
      else {
        s"${field.name}: $typeName"
      }
    }
    fields.mkString("  readonly ", ";\n  readonly ", ";\n").foreach {
      out.print
    }
    out.println("}")
  }

}
