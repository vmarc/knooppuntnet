package kpn.tools.code.typescript

import kpn.tools.code.domain.ClassInfo

import java.io.File
import java.io.PrintStream

class TypescriptWriter {

  private val root = "/Users/marc/wrk/projects/knooppuntnet/server/src/main/scala/kpn/api/common"
  private val targetDir = "/Users/marc/wrk/projects/knooppuntnet/frontend/src/app/api"

  def generate(classInfos: Seq[ClassInfo]): Unit = {
    classInfos.filterNot(_.isEnum).foreach(generateCaseClass)
    classInfos.filter(_.isEnum).foreach(generateEnumeration)
  }

  private def generateCaseClass(classInfo: ClassInfo): Unit = {
    val out = fileStream(classInfo)
    new TypescriptClassWriter(out, classInfo).write()
    out.close()
  }

  private def generateEnumeration(classInfo: ClassInfo): Unit = {
    val out = fileStream(classInfo)
    new TypeScriptEnumerationWriter(out, classInfo).write()
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
