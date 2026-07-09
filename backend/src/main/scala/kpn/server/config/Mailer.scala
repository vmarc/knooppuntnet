package kpn.server.config

trait Mailer {

  def send(subject: String, text: String): Unit
}
