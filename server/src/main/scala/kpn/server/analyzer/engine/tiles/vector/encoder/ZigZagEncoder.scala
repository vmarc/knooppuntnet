package kpn.server.analyzer.engine.tiles.vector.encoder

object ZigZagEncoder {
  def encode(n: Int): Int = { // https://developers.google.com/protocol-buffers/docs/encoding#types
    (n << 1) ^ (n >> 31)
  }
}
