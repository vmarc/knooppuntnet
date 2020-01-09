package kpn.server.api.analysis

trait Crypto {

  def encrypt(string: String): String

  def decrypt(string: String): String
}
