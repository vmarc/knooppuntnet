package kpn.tools.code

import kpn.core.util.Log
import kpn.tools.code.codecs.Codecs
import kpn.tools.code.domain.ClassInfo
import kpn.tools.code.typescript.TypescriptWriter

object CodeGenerator {
  def main(args: Array[String]): Unit = {
    new CodeGenerator().generate()
  }
}

class CodeGenerator {

  private val log = Log(classOf[CodeGenerator])

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

    codeClassInfos.foreach { classInfo =>
      val fieldTypeNames = classInfo.fields.flatMap(_.classType.typeName)
      val arrayFieldTypeNames = classInfo.fields.flatMap(_.classType.arrayType.flatMap(_.arrayType.flatMap(_.typeName)))
      val mapKeyFieldTypeNames = classInfo.fields.flatMap(_.classType.mapTypes.toSeq.flatMap(a => a._1.typeName.toSeq ++ a._2.typeName.toSeq))
      val typeNames = (fieldTypeNames ++ arrayFieldTypeNames ++ mapKeyFieldTypeNames).filterNot(typeName => Seq("Long", "Int", "String", "Boolean", "Timestamp", "Day", "Double", "ObjectId").contains(typeName)).sorted.distinct
      //  typeNames.foreach { typeName =>
      //    if (!codeClassInfos.exists(_.className == typeName)) {
      //      log.error(s"${classInfo.fullName} > $typeName")
      //    }
      //  }
    }

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
