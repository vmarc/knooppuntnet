package kpn.core.tools.typescript

import kpn.api.common.data.raw.RawNode
import kpn.api.common.monitor.MonitorGroupProperties
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteUpdate
import org.apache.commons.io.FileUtils

import java.io.File
import java.io.PrintStream
import scala.jdk.CollectionConverters.*
import scala.reflect.runtime.universe.*

object TypescriptTool {

  val formClasses: Seq[String] = Seq(
    name(MonitorGroupProperties),
    name(MonitorRouteProperties),
    name(MonitorRouteUpdate),
  )

  def main(args: Array[String]): Unit = {
    new TypescriptTool().generate()
  }

  private def name(caseClass: Object): String = {
    caseClass.getClass.getSimpleName.replace("$", "")
  }
}

class TypescriptTool {

  private val root = "/Users/marc/wrk/projects/knooppuntnet/server/src/main/scala/kpn/api/common"

  private val targetDir = "/Users/marc/wrk/projects/knooppuntnet/frontend/src/app/api"

  private val ignoredClasses: Seq[String] = Seq(
    // following classes have been manually changed in Typescript after changing List to Array, enable again when switching to interfaces
  )

  def generate(): Unit = {

    val scalaClasses = {
      val mirror = runtimeMirror(classOf[RawNode].getClassLoader)
      scalaClassNames().map(className => mirror.staticClass(className))
    }

    scalaClasses.filter(isCaseClass).foreach(generateCaseClass)
    scalaClasses.filter(isEnumeration).foreach(generateEnumeration)

    println("end")
  }

  private def scalaClassNames(): Seq[String] = {
    val files = FileUtils.listFiles(new File(root), Array("scala"), true).asScala.toSeq
    files.flatMap { file =>
      if (ignoredClasses.exists(n => file.getName.endsWith(n + ".scala"))) {
        None
      }
      else {
        val className = file.getAbsolutePath.drop(root.length - "kpn/api/common".length).dropRight(".scala".length).replace('/', '.')
        Some(className)
      }
    }
  }

  private def isCaseClass(classSymbol: ClassSymbol): Boolean = {
    classSymbol.typeSignature.typeSymbol.toString.contains("NetworkNameMissing") ||
      classSymbol.typeSignature.members.collect({ case m: MethodSymbol if m.isCaseAccessor => m }).nonEmpty
  }

  private def isEnumeration(classSymbol: ClassSymbol): Boolean = {
    classSymbol.baseClasses.exists(_.name.toString.contains("EnumEntry"))
  }

  private def generateCaseClass(caseClass: ClassSymbol): Unit = {
    val classInfo = new ClassAnalyzer().analyze(caseClass.typeSignature)
    val out = fileStream(caseClass)
    new TypescriptWriter(out, classInfo).write()
    out.close()
  }

  private def generateEnumeration(enumeration: ClassSymbol): Unit = {
    val out = fileStream(enumeration)
    val values = enumeration.knownDirectSubclasses.map { sub =>
      s"'${sub.name}'"
    }.mkString(" | ")
    out.println("// this file is generated, please do not modify")
    out.println()
    out.println(s"export type ${enumeration.name.toString} = $values;")
    out.close()
  }

  private def fileStream(classSymbol: ClassSymbol): PrintStream = {
    val className = classSymbol.name.toString
    val packageName = classSymbol.fullName.dropRight(className.length + 1)
    val dirName = packageName.replaceAll("kpn.api.", "").replaceAll("\\.", "/")
    val fileName = dirName + "/" + CamelCaseUtil.toDashed(className) + ".ts"
    val file = new File(targetDir + "/" + fileName)
    file.getParentFile.mkdirs()
    new PrintStream(file)
  }
}
