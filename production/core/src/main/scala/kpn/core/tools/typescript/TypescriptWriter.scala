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
    if (classInfo.dependencies.nonEmpty) {
      classInfo.dependencies.foreach { dependency =>
        out.println(s"import {${dependency.className}} from '${dependency.fileName}';")
      }
      out.println()
    }
  }

  private def writeClass(): Unit = {
    out.println(s"export class ${classInfo.className} {")
    out.println()
    writeConstructor()
    writeDeserializer()
    out.println("}\n")
  }

  private def writeConstructor(): Unit = {
    val fields = classInfo.fields.map { field =>
      s"public ${field.name}?: ${field.classType.typeName}"
    }
    out.print(s"  constructor(")
    fields.mkString("", ",\n              ", ") {\n  }\n\n").foreach {
      out.print
    }
  }

  private def writeDeserializer(): Unit = {
    out.println(s"  public static fromJSON(jsonObject): ${classInfo.className} {")
    out.println(s"    if (!jsonObject) {")
    out.println(s"      return undefined;")
    out.println(s"    }")
    out.println(s"    const instance = new ${classInfo.className}();")
    classInfo.fields.foreach { field =>

      out.print(s"    instance.${field.name} = ")
      if (field.classType.primitive) {
        out.print(s"jsonObject.${field.name}")
      }
      else {
        field.classType.arrayType match {
          case Some(arrayClassType) =>
            if (arrayClassType.primitive) {
              out.print(s"jsonObject.${field.name}")
            }
            else {
              out.print(s"jsonObject.${field.name} ? jsonObject.${field.name}.map(json => ${arrayClassType.typeName}.fromJSON(json)) : []")
            }
          case _ =>
            out.print(s"${field.classType.typeName}.fromJSON(jsonObject.${field.name})")
        }
      }
      out.println(";")
    }
    out.println("    return instance;")
    out.println("  }")
  }
}
