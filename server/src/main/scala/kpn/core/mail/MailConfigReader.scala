package kpn.core.mail

import com.typesafe.config.ConfigFactory

class MailConfigReader {

  def read(): MailConfig = {
    val config = ConfigFactory.load
    val host = config.getString("mail.host")
    val port = config.getString("mail.port")
    val user = config.getString("mail.user")
    val password = config.getString("mail.password")
    val recipient = config.getString("mail.recipient")
    val debug = config.getBoolean("mail.debug")
    MailConfig(host, port, user, password, recipient, debug)
  }
}
