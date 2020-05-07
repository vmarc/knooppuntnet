package kpn.server.config

import javax.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class Mailer(
  emailSender: JavaMailSender,
  applicationName: String,
  @Value("${mail.from}") mailFrom: String,
  @Value("${mail.to}") mailTo: String
) {

  def send(subject: String, text: String): Unit = {
    val message = new SimpleMailMessage
    message.setFrom(mailFrom)
    message.setTo(mailTo)
    message.setSubject(subject)
    message.setText(text)
    emailSender.send(message)
  }

  @PreDestroy
  def onExit(): Unit = {
    send(s"$applicationName - Server stopped", "ok?")
  }
}
