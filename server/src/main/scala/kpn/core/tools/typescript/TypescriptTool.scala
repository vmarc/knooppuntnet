package kpn.core.tools.typescript

import kpn.api.common.monitor.MonitorGroupProperties
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.tools.typescript.TypescriptTool.excludedTraits
import kpn.tools.code.ClassId
import kpn.tools.code.ScalaCaseClassReader
import kpn.tools.code.domain.ClassInfo
import org.apache.commons.io.FileUtils

import java.io.File
import java.io.PrintStream
import scala.jdk.CollectionConverters.*

object TypescriptTool {

  val formClasses: Seq[String] = Seq(
    name(MonitorGroupProperties),
    name(MonitorRouteProperties),
    name(MonitorRouteUpdate),
  )

  val excludedTraits = Seq(
    "LatLon",
    "Tagable",
    "Meta",
    "Element",
    "RawElement"
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
    val scalaCaseClassReader = new ScalaCaseClassReader()
    val classInfos = scalaClassIds().filterNot(ci => excludedTraits.contains(ci.className)).map(scalaCaseClassReader.read)

    classInfos.filterNot(_.isEnum).foreach(generateCaseClass)
    classInfos.filter(_.isEnum).foreach(generateEnumeration)

    println("end")
  }

  private def scalaClassIds(): Seq[ClassId] = {
    val files = FileUtils.listFiles(new File(root), Array("scala"), true).asScala.toSeq
    files.flatMap { file =>
      if (ignoredClasses.exists(n => file.getName.endsWith(s"$n.scala"))) {
        None
      }
      else {
        val path = file.getAbsolutePath.dropRight(".scala".length)
        val fullClassName = path.drop(root.length - "kpn/api/common".length).replace('/', '.')
        val className = fullClassName.split("\\.").last
        val packageName = fullClassName.dropRight(className.length + 1).replace('/', '.')
        Some(ClassId(className, packageName))
      }
    }
  }

  private def generateCaseClass(classInfo: ClassInfo): Unit = {
    val out = fileStream(classInfo)
    new TypescriptWriter(out, classInfo).write()
    out.close()
  }

  private def generateEnumeration(classInfo: ClassInfo): Unit = {
    val out = fileStream(classInfo)
    out.println("// this file is generated, please do not modify")
    out.println()
    out.println(s"export type ${classInfo.className} =")
    classInfo.enumValues.zipWithIndex.foreach { case (value, index) =>
      val lineEnd = if (index == classInfo.enumValues.length - 1) ";" else ""
      out.println(s"  | '$value'$lineEnd")
    }
    out.close()
  }

  private def fileStream(classInfo: ClassInfo): PrintStream = {
    val dirName = classInfo.packageName.replaceAll("kpn.api.", "").replaceAll("\\.", "/")
    val fileName = s"$dirName/${CamelCaseUtil.toDashed(classInfo.className)}.ts"
    val file = new File(s"$targetDir/$fileName")
    file.getParentFile.mkdirs()
    new PrintStream(file)
  }
}
