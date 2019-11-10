package kpn.core.replicate

import java.io.File

import kpn.api.custom.Timestamp
import kpn.core.util.Log

import scala.collection.mutable.ListBuffer
import scala.sys.process.Process
import scala.sys.process.ProcessLogger

object OverpassUpdate {

  def main(args: Array[String]): Unit = {
    new OverpassUpdate(
      new File("/mnt/ssd/kpn-test/overpass/bin/update_from_dir"),
      new File("/mnt/ssd/kpn-test/tmp")
    ).update(Timestamp(2015, 7, 13, 1, 59, 2))
  }
}

class OverpassUpdate(overpassUpdate: File, tmpDir: File) {

  private val log = Log(classOf[OverpassUpdate])

  def update(replicationTimestamp: Timestamp): Unit = {

    val bin = overpassUpdate.getAbsolutePath
    val source = "--osc-dir=" + tmpDir.getAbsolutePath
    val version = "--version=" + replicationTimestamp.iso.replaceAll("\\:", "\\\\:")

    val out = ListBuffer[String]()
    val err = ListBuffer[String]()

    val logger = ProcessLogger(line => out.append(line), line => err.append(line))
    val command = Seq(bin, source, version, "--keep-attic", "--flush-size=0")

    log.debug(command.mkString(" "))

    val t1 = System.currentTimeMillis()
    val status = Process(command).!(logger)
    val t2 = System.currentTimeMillis()
    val elapsed = t2 - t1

    val filteredOut = out.
      filterNot(_ == "Reading XML file ... finished reading nodes. Flushing to database ...... done.").
      filterNot(_ == "Reading XML file ... finished reading ways. Flushing to database ...... done.").
      filterNot(_ == "Reading XML file ... finished reading relations. Flushing to database ...... done.").
      filterNot(_ == "Update complete.")

    val message = (filteredOut ++ err).mkString("\n")

    if (status != 0) {
      val commandString = command.mkString(" ")
      val indentedOutput = (out ++ err).mkString("\n  ", "\n  ", "")
      throw new RuntimeException(s"""Could not execute command,status=$status, ${elapsed}ms: "$commandString" $indentedOutput""")
    }
    else {
      if (filteredOut.isEmpty) {
        log.debug(s"OK (${elapsed}ms)")
      }
      else {
        val indentedOutput = message.split("\n").mkString("\n  ", "\n  ", "")
        log.error(s"""Update not ok (${elapsed}ms}) $indentedOutput""")
      }
    }
  }
}
