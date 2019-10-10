package kpn.core.mail

trait Mail {
  def send(subject: String, text: String): Unit
}
