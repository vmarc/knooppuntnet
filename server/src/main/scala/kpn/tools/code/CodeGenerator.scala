package kpn.tools.code

import kpn.tools.code.codecs.Codecs
import kpn.tools.code.domain.ClassInfo
import kpn.tools.code.typescript.TypescriptWriter

object CodeGenerator {
  def main(args: Array[String]): Unit = {
    new CodeGenerator().generate()
  }
}

class CodeGenerator {

  def generate(): Unit = {
    val classInfos = ClassInfoReader.collectClassInfos()
    generateCodecs(classInfos)
    generateTypescript(classInfos)
  }

  private def generateTypescript(classInfos: Seq[ClassInfo]): Unit = {
    val apiCLassInfos = classInfos.filter(_.packageName.startsWith("kpn.api.common"))
    new TypescriptWriter().generate(apiCLassInfos)
  }

  private def generateCodecs(classInfos: Seq[ClassInfo]): Unit = {

    val codeClassInfos = collectCodecClassInfos(classInfos)

    val codecWriter = new CodecWriter()
    codeClassInfos.foreach(codecWriter.write)

    val codecProviderWriter = new CodecProviderWriter()
    val classIds = Codecs.customCodecs.filterNot(_.className == "ApiResponse") ++ codeClassInfos.map(classInfo => ClassId(classInfo.className, classInfo.packageName))
    codecProviderWriter.write(classIds)
  }

  private def collectCodecClassInfos(classInfos: Seq[ClassInfo]): Seq[ClassInfo] = {
    // Filter out classes that have custom codecs or are test classes
    classInfos.filterNot { classInfo =>
      Codecs.customCodecs.contains(ClassId(classInfo.className, classInfo.packageName)) ||
        classInfo.fullName == "kpn.database.tools.TestDoc"
    }
  }
}
