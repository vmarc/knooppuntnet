package kpn.tools.code

import kpn.api.common.data.raw.RawNode
import kpn.tools.code.ClassInfoReader.ignoreClassPatterns
import kpn.tools.code.ClassInfoReader.root
import kpn.tools.code.domain.ClassInfo
import org.apache.commons.io.FileUtils

import java.io.File
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.reflect.runtime.universe.runtimeMirror

object ClassInfoReader {
  private val root = "target/classes"
  private val ignoreClassPatterns = Seq(
    "AutoflushingFileHistory",
    "Codec"
  )

  def collectClassInfos(): Seq[ClassInfo] = {
    new ClassInfoReader().collectClassInfos()
  }
}

class ClassInfoReader {

  private val mirror = runtimeMirror(classOf[RawNode].getClassLoader)

  def collectClassInfos(): Seq[ClassInfo] = {
    val files = FileUtils.listFiles(new File(root), Array("class"), true).asScala.toSeq
    val classInfos = files.flatMap(analyzeFile)
    assembleEnumClassInfos(classInfos)
  }

  private def assembleEnumClassInfos(classInfos: Seq[ClassInfo]): Seq[ClassInfo] = {
    val enumEntries = classInfos.filter(_.isEnumEntry)
    classInfos.flatMap { classInfo =>
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
