package kpn.core.tools.route

object IdsFile {

  def read(filename: String): Seq[Long] = {
    val source = scala.io.Source.fromFile(filename)
    val ids = source.getLines().toSeq.map(_.toLong)
    source.close()
    ids
  }

}
