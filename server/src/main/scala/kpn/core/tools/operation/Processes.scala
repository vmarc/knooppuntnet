package kpn.core.tools.operation

object Processes {

  def webServerProcesses(lines: List[String]): Seq[ProcessInfo] = {
    assertHeader(lines.head)
    Seq(
      processInfo(lines.tail, "server", "/kpn/java/bin/java -Dname=server "),
      processInfo(lines.tail, "server-experimental", "/kpn/java/bin/java -Dname=server-experimental "),
      processInfo(lines.tail, "server-mail", "/kpn/java/bin/java -Dname=server-mail "),
      processInfo(lines.tail, "nginx", "nginx: master process"),
      processInfo(lines.tail, "mongod", "mongod --config /kpn/conf/mongod.conf"),
    )
  }

  def analysisServerProcesses(lines: List[String]): Seq[ProcessInfo] = {
    Seq(
      processInfo(lines.tail, "main-dispatcher", "/kpn/overpass/bin/dispatcher"),
      processInfo(lines.tail, "replicator", "name=replicator "),
      processInfo(lines.tail, "updater", "name=updater "),
      processInfo(lines.tail, "server", "/kpn/java/bin/java -Dname=server"),
      processInfo(lines.tail, "lsyncd", "lsyncd /kpn/conf/lsyncd.conf"),
      processInfo(lines.tail, "nginx", "nginx: master process"),
      processInfo(lines.tail, "update_from_dir", "/kpn/overpass/bin/update_from_dir"),
    ) ++ processInfos(lines.tail, "query", "overpass/bin/osm3s_query")
  }

  private def processInfo(lines: Seq[String], name: String, signature: String): ProcessInfo = {
    val status = lines.filterNot(_.contains("nohup")).find(_.contains(signature)).map(processStatus)
    ProcessInfo(name, status)
  }

  private def processInfos(lines: Seq[String], name: String, signature: String): Seq[ProcessInfo] = {
    lines.filter(_.contains(signature)).map { line =>
      ProcessInfo(name, Some(processStatus(line)))
    }
  }

  private def processStatus(line: String): ProcessStatus = {
    val pl = ProcessLine(line)
    ProcessStatus(pl.pid, pl.start, pl.elapsed)
  }

  private def assertHeader(line: String): Unit = {
    if (line != "UID          PID    PPID  C STIME TTY          TIME CMD") {
      throw new IllegalArgumentException("unexpected output from ps -ef")
    }
  }
}
