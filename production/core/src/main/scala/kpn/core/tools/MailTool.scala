package kpn.core.tools

import kpn.core.mail.Mail
import kpn.core.mail.MailConfigReader
import kpn.core.mail.MailImpl

object MailTool {
  def main(args: Array[String]): Unit = {
    val config = new MailConfigReader().read()
    val mail: Mail = new MailImpl(config)
    mail.send("Test", "Hello World")
    println("Done")
  }
}
