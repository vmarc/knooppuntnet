package kpn.tools.code

import kpn.tools.code.domain.ClassField
import kpn.tools.code.domain.ClassInfo

import java.io.PrintStream

class JavaClassWriter(out: PrintStream, sourceFileContents: String, classInfo: ClassInfo) {

  def write(): Unit = {
    out.println(s"package ${classInfo.packageName};")
    out.println()
    writeImports()
    writeContents()
    writeComment()
  }

  private def writeComment(): Unit = {
    out.println("/*")
    out.println(sourceFileContents.replaceAll("/\\*", "*** ").replaceAll("\\*/", "*** "))
    out.println("*/")
  }

  private def writeImports(): Unit = {
    val imports = dependencyImports()
    if (imports.nonEmpty) {
      out.println(imports.mkString("", "\n", "\n"))
    }

    val standardImports = javaUtilDependencyImports() ++ collectionDependencyImports()
    if (standardImports.nonEmpty) {
      out.println(standardImports.mkString("", "\n", "\n"))
    }
  }

  private def dependencyImports(): Seq[String] = {
    val dependencies = classInfo.dependencies
      .filterNot(_.className == classInfo.className)

    val sortedDependencies = sortDependencies(dependencies)

    sortedDependencies.map(formatImportStatement)
  }

  private def collectionDependencyImports(): Seq[String] = {
    val dependencies = classInfo.collectionDependencies
    val sortedDependencies = sortDependencies(dependencies)
    sortedDependencies.map(formatImportStatement)
  }

  private def javaUtilDependencyImports(): Seq[String] = {
    val dependencies = classInfo.javaUtilDependencies
    val sortedDependencies = sortDependencies(dependencies)
    sortedDependencies.map(formatImportStatement)
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
    s"import ${dependency.packageName}.${dependency.className};"
  }

  private def writeContents(): Unit = {
    out.println(s"public record ${classInfo.className}(")
    val fieldDefinitions = classInfo.fields.map(formatField)
    fieldDefinitions.mkString("  ", ",\n  ", "\n").foreach(out.print)
    out.println(") {")
    out.println("}\n")
  }

  private def formatField(field: ClassField): String = {
    val fieldName = field.name // if (field.classType.optional) s"${field.name}?" else field.name

    field.classType.typeName match {
      case Some(typeName) =>
        val fullTypeName = if (field.classType.optional) s"Optional<$typeName>" else typeName
        s"$fullTypeName $fieldName"
      case None =>
        field.classType.arrayType match {
          case Some(arrayType) =>
            arrayType.typeName match {
              case Some(typeName) =>
                val collectionType = field.classType.arrayTypeClass match {
                  case Some("Seq") => "ImmutableList"
                  case Some("Set") => "ImmutableSet"
                  case Some("Vector") => "ImmutableList"
                  case _ => throw new IllegalArgumentException(s"Unknown collection type: ${field.classType.arrayTypeClass}")
                }

                s"$collectionType<${mapScalaToJavaType(typeName)}> $fieldName"

              case None =>
                throw new IllegalArgumentException(s"Missing type name for array type in field ${field.name}")
            }
          case None =>
            throw new IllegalArgumentException(s"Missing type information for field ${field.name}")
        }
    }
  }

  private def mapScalaToJavaType(typeName: String): String = typeName match {
    case "Int" => "Integer"
    case "PlanCoordinate" => "Coordinate"
    case _ => typeName
  }
}
