package kpn.core.tools.typescript

import java.io.File
import java.io.PrintStream

import kpn.shared.data.raw.RawNode
import org.apache.commons.io.FileUtils

import scala.reflect.runtime.universe._

object TypescriptTool {
  def main(args: Array[String]): Unit = {
    new TypescriptTool().generate()
  }
}

class TypescriptTool() {

  val root = "/home/marcv/wrk/projects/knooppuntnet/server/src/main/scala/kpn/shared"

  val targetDir = "/home/marcv/wrk/projects/knooppuntnet/client/src/app"

  val ignoredClasses: Seq[String] = Seq(
    "ApiResponse",
    "Subset",
    "Country",
    "NetworkType",
    "Timestamp",
    "Fact",
    "FactLevel",
    "Tags",
    "Tag",
    "RouteMemberInfo", // Ignored because of reference to WayDirection
    "Statistics", // temporarily ignored because Map fromJSON does not work yet
    "Change", // problem with RawElement
    "Relation" // problem with Member
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
    import collection.JavaConverters._
    val files = FileUtils.listFiles(new File(root), Array("scala"), true).asScala.toSeq
    files.flatMap { file =>
      if (ignoredClasses.exists(n => file.getName.endsWith(n + ".scala"))) {
        None
      }
      else {
        val className = file.getAbsolutePath.drop(root.length - "kpn/shared".length).dropRight(".scala".length).replace('/', '.')
        Some(className)
      }
    }
  }

  private def isCaseClass(scalaType: Type): Boolean = {
    scalaType.typeSymbol.toString.contains("NetworkNameMissing") ||
      scalaType.members.collect({ case m: MethodSymbol if m.isCaseAccessor => m }).nonEmpty
  }

}
