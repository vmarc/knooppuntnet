package kpn.server.api.analysis.pages.poi.analyzers

import kpn.server.api.analysis.pages.poi.PoiAnalysisContext
import kpn.server.api.analysis.pages.poi.PoiAnalyzer
import org.apache.commons.codec.digest.DigestUtils

object PoiImageAnalyzer extends PoiAnalyzer {

  val imagePrefixes = Seq(
    "File:",
    "http://commons.wikimedia.org/wiki/File%3A",
    "https://commons.wikimedia.org/wiki/File%3A",
    "http://commons.wikimedia.org/wiki/File:",
    "https://commons.wikimedia.org/wiki/File:"
  )

  def analyze(context: PoiAnalysisContext): PoiAnalysisContext = {
    new PoiImageAnalyzer(context).analyze
  }
}

class PoiImageAnalyzer(context: PoiAnalysisContext) {

  def analyze: PoiAnalysisContext = {

    context.poi.tags("image") match {
      case None => context
      case Some(tagValue) =>

        val fileName = PoiImageAnalyzer.imagePrefixes.flatMap { imagePrefix =>
          if (tagValue.startsWith(imagePrefix)) {
            Some(tagValue.substring(imagePrefix.length))
          }
          else {
            None
          }
        }

        if (fileName.nonEmpty) {
          val image = s"https://upload.wikimedia.org/wikipedia/commons/${md5HexUrl(fileName.head)}"
          context.copy(
            analysis = context.analysis.copy(image = Some(image)),
            processedTagKeys = context.processedTagKeys :+ "image"
          )
        }
        else if (isImage(tagValue)) {
          context.copy(
            analysis = context.analysis.copy(image = Some(tagValue)),
            processedTagKeys = context.processedTagKeys :+ "image"
          )
        }
        else {
          context.copy(
            analysis = context.analysis.copy(imageLink = Some(tagValue)),
            processedTagKeys = context.processedTagKeys :+ "image"
          )
        }
    }
  }

  private def md5HexUrl(fileName: String): String = {
    val messageDigest = DigestUtils.md5Hex(fileName.replace(" ", "_"))
    val hash1 = messageDigest.substring(0, 1)
    val hash2 = messageDigest.substring(0, 2)
    hash1 + "/" + hash2 + "/" + fileName
  }

  private def isImage(tagValue: String): Boolean = {
    val name = tagValue.toLowerCase()
    name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif") || name.endsWith(".png")
  }

}
