package kpn.tools.code

import kpn.api.common.data.raw.RawNode
import kpn.tools.code.codecs.Codecs
import kpn.tools.code.domain.ClassInfo
import org.apache.commons.io.FileUtils

import java.io.File
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.reflect.runtime.universe.runtimeMirror

object RootDocReader {
  val scalaCaseClassReader = new ScalaCaseClassReader()

  def main(args: Array[String]): Unit = {
    new RootDocReader().read()
  }
}

class RootDocReader {

  private val ignoreClassPatterns = Seq(
    "AutoflushingFileHistory",
    "Codec"
  )

  private val root = "target/classes"
  private val mirror = runtimeMirror(classOf[RawNode].getClassLoader)

  def read(): Unit = {

    val codecWriter = new CodecWriter()

    val files = FileUtils.listFiles(new File(root), Array("class"), true).asScala.toSeq
    val classInfos: Seq[ClassInfo] = files.flatMap(analyzeFile)

    val enumEntries = classInfos.filter(_.isEnumEntry)

    val updatedClassInfos = classInfos.flatMap { classInfo =>
      if (classInfo.isEnumEntry) {
        None
      }
      else if (classInfo.isEnum) {
        val entries = enumEntries.filter(_.enumClassName == classInfo.enumClassName)
        val enumValues = entries.flatMap(_.enumValue)
        Some(classInfo.copy(enumValues = enumValues))
      }
      else {
        Some(classInfo)
      }
    }

    val codeClassInfos = updatedClassInfos.filterNot { classInfo =>
      Codecs.customCodecs.contains(ClassId(classInfo.className, classInfo.packageName)) ||
        classInfo.fullName == "kpn.database.tools.TestDoc"
    }

    codeClassInfos.foreach(codecWriter.write)

    val classIds = Codecs.customCodecs.filterNot(_.className == "ApiResponse") ++ codeClassInfos.map(classInfo => ClassId(classInfo.className, classInfo.packageName))

    new CodecProviderWriter().write(classIds)
  }

  private def analyzeFile(file: File): Option[ClassInfo] = {
    val path = file.getPath.dropRight(".class".length)
    val fullClassName = path.drop(root.length + 1).replace('/', '.')

    if (ignoreClassPatterns.exists(fullClassName.contains)) {
      None
    }
    else {
      val clazz = new Clazz(mirror.staticClass(fullClassName).typeSignature)

      if (clazz.isEnumEntry) {
        Some(
          ClassInfo(
            className = clazz.className,
            packageName = clazz.packageName,
            fields = Seq.empty,
            enumClassName = Some(clazz.enumClassName),
            isEnumEntry = true,
          )
        )
      }
      else {
        if (clazz.isEnum) {
          Some(
            ClassInfo(
              className = clazz.className,
              packageName = clazz.packageName,
              fields = Seq.empty,
              enumClassName = Some(clazz.enumClassName),
              isEnum = true,
            )
          )
        }
        else {
          if (clazz.isStorable || clazz.isApi) {
            Some(
              ClassInfo(
                className = clazz.className,
                packageName = clazz.packageName,
                fields = clazz.fields,
              )
            )
          }
          else {
            None
          }
        }
      }
    }
  }
}
