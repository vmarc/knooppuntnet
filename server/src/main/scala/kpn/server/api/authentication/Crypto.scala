package kpn.server.api.authentication

trait Crypto {

  def encrypt(string: String): String

  def decrypt(string: String): String
}
