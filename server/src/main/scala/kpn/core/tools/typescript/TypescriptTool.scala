package kpn.core.tools.typescript

import kpn.api.common.data.raw.RawNode
import kpn.api.common.monitor.MonitorGroupProperties
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteUpdate
import org.apache.commons.io.FileUtils

import java.io.File
import java.io.PrintStream
import scala.jdk.CollectionConverters._
import scala.reflect.runtime.universe._

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

class TypescriptTool() {

  val root = "/home/vmarc/wrk/projects/knooppuntnet/server/src/main/scala/kpn/api/common"

  val targetDir = "/home/vmarc/wrk/projects/knooppuntnet/client/libs/api/src/lib"

  val ignoredClasses: Seq[String] = Seq(
    // following classes have been manually changed in Typescript after changing List to Array, enable again when switching to interfaces
  )

  def generate(): Unit = {

    val mirror = runtimeMirror(classOf[RawNode].getClassLoader)
    val scalaTypes: Seq[Type] = scalaClassNames().map(className => mirror.staticClass(className).typeSignature)
    val caseClasses: Seq[Type] = scalaTypes.filter(isCaseClass)

    caseClasses.foreach { caseClass =>
      val classInfo = new ClassAnalyzer().analyze(caseClass)
      val file = new File(targetDir + "/" + classInfo.fileName)
      file.getParentFile.mkdirs()
      val out = new PrintStream(file)
      new TypescriptWriter(out, classInfo).write()
      out.close()
    }

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

  private def isCaseClass(scalaType: Type): Boolean = {
    scalaType.typeSymbol.toString.contains("NetworkNameMissing") ||
      scalaType.members.collect({ case m: MethodSymbol if m.isCaseAccessor => m }).nonEmpty
  }
}
