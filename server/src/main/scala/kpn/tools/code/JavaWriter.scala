package kpn.tools.code

import kpn.tools.code.domain.ClassInfo
import org.apache.commons.io.FileUtils

import java.io.File
import java.io.PrintStream

class JavaWriter {

  private val root = "/Users/marc/wrk/projects/knooppuntnet/server/src/main/scala/"
  private val targetDir = "/Users/marc/wrk/projects/knooppuntnet/backend/api/src/main/java"

  def generate(classInfos: Seq[ClassInfo]): Unit = {
    classInfos.filterNot(_.isEnum).foreach(generateCaseClass)
    classInfos.filter(_.isEnum).foreach(generateEnumeration)
  }

  private def generateCaseClass(classInfo: ClassInfo): Unit = {
    val javaFile = file(classInfo)
    val scalaFile = new File(s"$root${classInfo.packageName.replaceAll("\\.", "/")}/${classInfo.className}.scala")
    val sourceFileContents = FileUtils.readFileToString(scalaFile, "UTF-8")

    val out = fileStream(javaFile, classInfo)
    new JavaClassWriter(out, sourceFileContents, classInfo).write()
    out.close()
  }

  private def generateEnumeration(classInfo: ClassInfo): Unit = {
    val javaFile = file(classInfo)
    val out = fileStream(javaFile, classInfo)
    new JavaEnumerationWriter(out, classInfo).write()
    out.close()
  }

  private def fileStream(file: File, classInfo: ClassInfo): PrintStream = {
    file.getParentFile.mkdirs()
    new PrintStream(file)
  }

  private def file(classInfo: ClassInfo): File = {
    val dirName = classInfo.packageName.replaceAll("\\.", "/")
    val fileName = s"$dirName/${classInfo.className}.java"
    new File(s"$targetDir/$fileName")
  }
}
