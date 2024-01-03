package kpn.server.config

import org.apache.commons.codec.binary.Base64.encodeBase64String

import javax.crypto.KeyGenerator

object CryptoGenerateKey {
  def main(args: Array[String]): Unit = {
    val keyGen = KeyGenerator.getInstance("AES")
    keyGen.init(256)
    println(encodeBase64String(keyGen.generateKey.getEncoded))
  }
}
