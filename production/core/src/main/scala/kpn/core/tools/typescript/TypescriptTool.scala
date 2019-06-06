package kpn.core.tools.typescript

import java.io.File
import java.io.PrintStream

import kpn.shared.data.raw.RawNode
import scalax.file.Path

import scala.reflect.runtime.universe._

object TypescriptTool {
  def main(args: Array[String]): Unit = {
    new TypescriptTool().generate()
  }
}

class TypescriptTool() {

  val root = "/home/marcv/wrk/projects2/knooppuntnet/production/shared/src/main/scala"

  val targetDir = "/home/marcv/wrk/projects2/knooppuntnet/client/src/app"

  val ignoredClasses: Seq[String] = Seq(
    "ApiResponse",
    "Subset",
    "Country",
    "NetworkType",
    "Timestamp",
    "Fact",
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
    Path.fromString(root).descendants().toSeq.flatMap { path =>
      if (path.isFile && path.name.endsWith(".scala")) {
        if (ignoredClasses.exists(n => path.path.endsWith("/" + n + ".scala"))) {
          None
        }
        else {
          val className = path.path.drop(root.length + 1).dropRight(".scala".length).replace('/', '.')
          Some(className)
        }
      }
      else {
        None
      }
    }
  }

  private def isCaseClass(scalaType: Type): Boolean = {
    scalaType.typeSymbol.toString.contains("NetworkNameMissing") ||
      scalaType.members.collect({ case m: MethodSymbol if m.isCaseAccessor => m }).nonEmpty
  }

}
