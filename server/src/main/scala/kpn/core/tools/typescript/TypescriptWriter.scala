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

  private def writeComment(): Unit = {
    out.println("// this class is generated, please do not modify")
    out.println()
  }

  private def writeImports(): Unit = {
    val imports = listImports() ++ mapImports() ++ dependencyImports()
    if (imports.nonEmpty) {
      out.println(imports.mkString("", "\n", "\n"))
    }
  }

  private def listImports(): Seq[String] = {
    if (classInfo.fields.exists(_.classType.isArray)) {
      Seq("""import {List} from "immutable";""")
    }
    else {
      Seq()
    }
  }

  private def mapImports(): Seq[String] = {
    if (classInfo.fields.exists(_.classType.isMap)) {
      Seq("""import {Map} from "immutable";""")
    }
    else {
      Seq()
    }
  }

  private def dependencyImports(): Seq[String] = {
    classInfo.dependencies.map { dependency =>
      s"""import {${dependency.className}} from "${dependency.fileName}";"""
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
      val typeName = field.classType.typeName.replaceAll("Array<", "List<")
      s"${field.name}: $typeName"
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
      else {
        field.classType.arrayType match {
          case Some(arrayClassType) =>
            if (arrayClassType.primitive) {
              s"      jsonObject.${field.name} ? List(jsonObject.${field.name}) : List()"
            }
            else {
              s"      jsonObject.${field.name} ? List(jsonObject.${field.name}.map(json => ${arrayClassType.typeName}.fromJSON(json))) : List()"
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
}
