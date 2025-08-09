package kpn.tools.code

import kpn.tools.code.codecs.Codecs
import kpn.tools.code.domain.ClassInfo

import scala.collection.mutable
import scala.io.Source

object RootDocReader {
  val scalaCaseClassReader = new ScalaCaseClassReader()

  def main(args: Array[String]): Unit = {

    val rootClassIds = readClassIds() ++ Codecs.customCodecs ++ Codecs.extraCodecs
    val rootClassInfos = rootClassIds.map(classId => scalaCaseClassReader.read(classId))

    val found = mutable.Map(rootClassInfos.map(classInfo => classInfo.key -> classInfo): _*)

    val all = rootClassInfos.flatMap { classInfo =>
      collectDependencies(found, classInfo)
    }

    val codecWriter = new CodecWriter()
    found.values.foreach { classInfo =>
      if (!Codecs.customCodecs.contains(ClassId(classInfo.className, classInfo.packageName))) {
        codecWriter.write(classInfo)
      }
    }

    val codecProviderWriter = new CodecProviderWriter()
    codecProviderWriter.write(found.values.toSeq)
  }

  private def collectDependencies(found: mutable.Map[String, ClassInfo], info: ClassInfo): Seq[ClassInfo] = {
    println(info.className)
    val missingClassIds = info.dependencies.filter(classId => !found.contains(classId.key)).filterNot(_.className == "Member")
    val classInfos = missingClassIds.map(classId => scalaCaseClassReader.read(classId))
    found.addAll(classInfos.map(classInfo => classInfo.key -> classInfo))
    classInfos.flatMap { classInfo =>
      println(s"  ${classInfo.className}")
      collectDependencies(found, classInfo)
    }
  }

  private def readClassIds(): Seq[ClassId] = {
    val filename = "src/main/scala/kpn/database/base/Database.scala"
    val source = Source.fromFile(filename)
    try {
      val lines = source.getLines().toSeq

      val collectionPattern = """.*DatabaseCollection\[(\w+)\]""".r

      val excludedNames = Seq("WithStringId")

      val classNames = lines
        .flatMap(collectionPattern.findFirstMatchIn)
        .map(_.group(1))
        .filterNot(excludedNames.contains)
        .sorted
        .distinct

      val importLines = lines.filter(_.startsWith("import "))
      val classIds = classNames.map { className =>
        val pattern = s".*\\.$className$$".r
        val packageLine = importLines.find(pattern.matches)
        packageLine match {
          case None => throw new RuntimeException(s"cannot find import for $className in $filename")
          case Some(line) =>
            val packageName = line.dropRight(className.length + 1).drop("import ".length)
            ClassId(className, packageName)
        }
      }

      classIds
    }
    finally {
      source.close()
    }
  }
}
