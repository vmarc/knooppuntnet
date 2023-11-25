package kpn.backend

trait Crypto {

  def encrypt(string: String): String

  def decrypt(string: String): String
}
