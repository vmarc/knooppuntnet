package kpn.core.loadOld

import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

import kpn.shared.data.raw.RawData

class OsmDataZipWriter {

  def write(osmData: RawData, zipFileName: String): Unit = {
    val file = new File(zipFileName)
    //file.mkdirs

    val outputStream = new FileOutputStream(file)
    val zip = new ZipOutputStream(outputStream)
    zip.putNextEntry(new ZipEntry("osm-data.xml"))
    val writer = new PrintWriter(zip)
    new OsmDataXmlWriter(osmData, writer).print()
    writer.close()
  }
}
