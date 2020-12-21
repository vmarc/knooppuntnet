package kpn.core.tools.typescript

import kpn.api.common.BoundsI
import kpn.api.common.changes.details.ChangeKeyI
import kpn.api.common.data.raw.RawNode
import kpn.api.common.monitor.LongdistanceRouteChange
import kpn.api.common.monitor.LongdistanceRouteChangePage
import kpn.api.common.monitor.LongdistanceRouteChangeSummary
import kpn.api.common.monitor.LongdistanceRouteChangesPage
import kpn.api.common.monitor.LongdistanceRouteDetail
import kpn.api.common.monitor.LongdistanceRouteDetailsPage
import kpn.api.common.monitor.LongdistanceRouteMapPage
import kpn.api.common.monitor.LongdistanceRouteNokSegment
import kpn.api.common.monitor.LongdistanceRouteSegment
import kpn.api.common.monitor.LongdistanceRoutesPage
import kpn.api.common.monitor.MonitorAdminGroupPage
import kpn.api.common.monitor.MonitorRouteChangePage
import kpn.api.common.monitor.MonitorRouteChangeSummary
import kpn.api.common.monitor.MonitorRouteChangesPage
import kpn.api.common.monitor.MonitorRouteDetail
import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorGroup
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteNokSegment
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.common.monitor.MonitorGroupPage
import kpn.api.common.monitor.RouteGroupDetail
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteChange
import org.apache.commons.io.FileUtils

import java.io.File
import java.io.PrintStream
import scala.jdk.CollectionConverters._
import scala.reflect.runtime.universe._

object TypescriptTool {
  def main(args: Array[String]): Unit = {
    new TypescriptTool().generate()
  }
}

class TypescriptTool() {

  val root = "/home/marcv/wrk/projects1/knooppuntnet/server/src/main/scala/kpn/api/common"

  val targetDir = "/home/marcv/wrk/projects1/knooppuntnet/client/src/app"

  val ignoredClasses: Seq[String] = Seq(
  )

  val newClasses = Seq(
    classOf[MonitorRoute], // not used in API ?
    classOf[MonitorGroupPage],
    classOf[MonitorRouteDetail],
    classOf[MonitorRouteChangesPage],
    classOf[MonitorRouteDetail],
    classOf[MonitorRouteDetailsPage],
    classOf[MonitorRouteMapPage],
    classOf[MonitorRouteNokSegment],
    classOf[MonitorRouteSegment],
    classOf[MonitorRouteChangePage],
    classOf[MonitorRouteChange],
    classOf[MonitorRouteChangeSummary],
    classOf[MonitorGroup],
    classOf[MonitorAdminGroupPage],
    classOf[BoundsI],
    classOf[ChangeKeyI],
    classOf[MonitorGroupsPage],
    classOf[RouteGroupDetail],
    classOf[LongdistanceRouteChange],
    classOf[LongdistanceRouteChangePage],
    classOf[LongdistanceRouteChangesPage],
    classOf[LongdistanceRouteChangeSummary],
    classOf[LongdistanceRouteDetail],
    classOf[LongdistanceRouteDetailsPage],
    classOf[LongdistanceRouteMapPage],
    classOf[LongdistanceRouteNokSegment],
    classOf[LongdistanceRouteSegment],
    classOf[LongdistanceRoutesPage],
  )

  def generate(): Unit = {

    val mirror = runtimeMirror(classOf[RawNode].getClassLoader)
    val scalaTypes: Seq[Type] = scalaClassNames().map(className => mirror.staticClass(className).typeSignature)
    val caseClasses: Seq[Type] = scalaTypes.filter(isCaseClass)

    val newClassNames = newClasses.map(_.getSimpleName)

    caseClasses.foreach { caseClass =>
      val className = caseClass.typeSymbol.name.toString
      val classInfo = new ClassAnalyzer().analyze(caseClass)
      val file = new File(targetDir + "/" + classInfo.fileName)
      file.getParentFile.mkdirs()
      val out = new PrintStream(file)
      if (newClassNames.contains(className)) {
        new TypescriptWriter(out, classInfo).writeInterface()
      }
      else {
        new TypescriptWriter(out, classInfo).write()
      }
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
