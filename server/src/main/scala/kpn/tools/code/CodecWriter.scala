package kpn.tools.code

import kpn.tools.code.domain.ClassField
import kpn.tools.code.domain.ClassInfo
import kpn.tools.code.domain.ClassType

import java.io.File
import java.io.PrintStream

object CodecWriter {
  def main(args: Array[String]): Unit = {
    val classInfo = ClassInfo(
      "RawNode",
      "kpn.api.common.data.raw",
      Seq(
        ClassField(
          "id",
          ClassType(
            typeName = Some("Long"),
          )
        ),
        ClassField(
          "latitude",
          ClassType(
            typeName = Some("String"),
          )
        ),
        ClassField(
          "longitude",
          ClassType(
            typeName = Some("String"),
          )
        ),
        ClassField(
          "version",
          ClassType(
            typeName = Some("Long"),
          )
        ),
        ClassField(
          "timestamp",
          ClassType(
            typeName = Some("Timestamp"),
            packageName = Some("kpn.api.custom"),
          )
        ),
        ClassField(
          "changeSetId",
          ClassType(
            typeName = Some("Long"),
          )
        ),
        ClassField(
          "tags",
          ClassType(
            arrayType = Some(
              ClassType(
                typeName = Some("Tag"),
                packageName = Some("kpn.api.custom"),
              )
            )
          )
        ),
      )
    )

    new CodecWriter().write(classInfo)
  }
}

class CodecWriter {
  def write(classInfo: ClassInfo): Unit = {

    val dirName = "src/main/scala/kpn/tools/code/codecs/generated"
    val file = new File(s"$dirName/${classInfo.className}Codec.scala")
    file.getParentFile.mkdirs()
    val out = new PrintStream(file)

    out.println("package kpn.tools.code.codecs.generated")
    out.println("")

    val fixedImportClasses = Seq(
      "kpn.tools.code.codecs.Codecs",
      "org.bson.BsonReader",
      "org.bson.BsonType",
      "org.bson.BsonWriter",
      "org.bson.codecs.Codec",
      "org.bson.codecs.DecoderContext",
      "org.bson.codecs.EncoderContext",
      "org.bson.codecs.configuration.CodecRegistry",
    )

    val importClass = s"${classInfo.packageName}.${classInfo.className}"
    val classTypes = classInfo.fields.flatMap { field => Seq(field.classType) ++ field.classType.arrayType.toSeq }

    val fieldImportClasses = classTypes.flatMap { classType =>
      classType.packageName match {
        case Some(packageName) =>
          classType.typeName match {
            case Some(typeName) => Some(s"$packageName.$typeName")
            case None => None
          }
        case None => None
      }
    }

    val importClasses = (Seq(importClass) ++ fixedImportClasses ++ fieldImportClasses).sorted.distinct

    importClasses.foreach { className =>
      out.println(s"import $className")
    }

    out.println("")

    out.println(s"class ${classInfo.className}Codec(registry: CodecRegistry) extends Codec[${classInfo.className}] {")

    printCodecs(out, classInfo)

    printDecodeMethod(out, classInfo)
    printEncodeMethod(out, classInfo)
    printGetEncoderClassMethod(out, classInfo)

    out.println("}")
    out.close()
  }

  private def printCodecs(out: PrintStream, classInfo: ClassInfo): Unit = {
    val classTypes = classInfo.fields.flatMap { field => field.classType.typeName.toSeq ++ field.classType.arrayType.toSeq.flatMap(_.typeName) }.sorted.distinct
    out.println("")
    classTypes.foreach { typeName =>
      val lowercaseTypeName = s"${typeName.head.toLower}${typeName.tail}"
      out.println(s"  private val ${lowercaseTypeName}Codec = registry.get(classOf[${typeName}])")
    }
  }

  private def printDecodeMethod(out: PrintStream, classInfo: ClassInfo): Unit = {

    out.println("")
    out.println("  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): RawNode = {")
    out.println("    bsonReader.readStartDocument()")

    out.println("")
    classInfo.fields.foreach { field =>
      val defaultValue = if (field.classType.typeName.contains("Long")) "0" else "null"
      out.println(s"    var ${field.name}: ${field.typeString} = $defaultValue")
    }

    out.println("")

    out.println("    while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {")
    out.println("      val fieldName = bsonReader.readName")
    classInfo.fields.zipWithIndex.foreach { case (field, index) =>
      out.println(s"      ${if (index == 0) "" else "else "}if (fieldName == \"${field.name}\") {")
      field.classType.arrayType match {
        case Some(arrayType) =>

          arrayType.typeName match {
            case Some(typeName) =>
              val lowercaseTypeName = s"${typeName.head.toLower}${typeName.tail}"
              out.println(s"        bsonReader.readStartArray()")
              out.println(s"        val valueBuffer = scala.collection.mutable.Buffer[$typeName]()")
              out.println(s"        while (bsonReader.readBsonType != BsonType.END_OF_DOCUMENT) {")
              out.println(s"          valueBuffer += ${lowercaseTypeName}Codec.decode(bsonReader, decoderContext)")
              out.println(s"        }")
              out.println(s"        bsonReader.readEndArray()")
              out.println(s"        ${field.name} = valueBuffer.toSeq")
            case None =>
              out.println(s"        // TODO implement ${field.name} = ???")
          }

        case None =>
          field.classType.typeName match {
            case Some(typeName) =>
              val lowercaseTypeName = s"${typeName.head.toLower}${typeName.tail}"
              out.println(s"        ${field.name} = ${lowercaseTypeName}Codec.decode(bsonReader, decoderContext)")
            case None =>
              out.println(s"        // TODO implement ${field.name} = ???")
          }
      }
      out.println("      }")
    }

    out.println(s"      else {")
    out.println(s"        Codecs.log.warn(s\"Unknown field name: $$fieldName in ${classInfo.className}Codec.decode()\")")
    out.println(s"        bsonReader.skipValue()")
    out.println(s"      }")
    out.println(s"    }")

    out.println("    bsonReader.readEndDocument()")
    out.println("    RawNode(")
    classInfo.fields.foreach { field =>
      out.println(s"      ${field.name},")
    }
    out.println("    )")
    out.println("  }")
  }

  private def printEncodeMethod(out: PrintStream, classInfo: ClassInfo): Unit = {
    out.println("")
    out.println(s"  override def encode(bsonWriter: BsonWriter, value: ${classInfo.className}, encoderContext: EncoderContext): Unit = {")
    out.println("    bsonWriter.writeStartDocument()")
    classInfo.fields.foreach { field =>
      out.println("")
      out.println(s"    bsonWriter.writeName(\"${field.name}\")")

      field.classType.arrayType match {
        case Some(arrayType) =>

          arrayType.typeName match {
            case Some(typeName) =>
              val lowercaseTypeName = s"${typeName.head.toLower}${typeName.tail}"
              out.println(s"    bsonWriter.writeStartArray()")
              out.println(s"    value.${field.name}.foreach(v => ${lowercaseTypeName}Codec.encode(bsonWriter, v, encoderContext))")
              out.println(s"    bsonWriter.writeEndArray()")

            case None =>
              out.println(s"    ??? // TODO implement ${field.name}")
          }

        case None =>
          field.classType.typeName match {
            case Some(typeName) =>
              val lowercaseTypeName = s"${typeName.head.toLower}${typeName.tail}"
              out.println(s"    ${lowercaseTypeName}Codec.encode(bsonWriter, value.${field.name}, encoderContext)")
            case None =>
              out.println(s"    ??? // TODO implement ${field.name}")
          }
      }
    }

    out.println("    bsonWriter.writeEndDocument()")
    out.println("  }")
  }

  private def printGetEncoderClassMethod(out: PrintStream, classInfo: ClassInfo): Unit = {
    out.println("")
    out.println(s"  override def getEncoderClass: Class[${classInfo.className}] = {")
    out.println(s"    classOf[${classInfo.className}]")
    out.println(s"  }")
  }
}
