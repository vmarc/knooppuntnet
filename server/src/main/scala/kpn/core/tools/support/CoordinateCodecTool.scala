package kpn.core.tools.support

import kpn.server.analyzer.engine.tiles.domain.CoordinateCodec

object CoordinateCodecTool {
  def main(args: Array[String]): Unit = {
    val encoded = "[[5.1234,51.4321],[1111000,1111000],[1111000,1111000]]"
    val decoded = CoordinateCodec.decode(encoded)
    println(decoded.map(c => s"[${c.x},${c.y}]").mkString("[", ",", "]"))
  }
}
