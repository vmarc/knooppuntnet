package kpn.server.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

import java.net.InetAddress
import javax.annotation.PreDestroy

@Component
@Profile(Array("!dev & web", "!dev & analysis"))
class MailerImpl(
  mailSender: JavaMailSender,
  applicationName: String,
  @Value("${mail.from}") mailFrom: String,
  @Value("${mail.to}") mailTo: String
) extends Mailer {

  override def send(subject: String, text: String): Unit = {
    val message = new SimpleMailMessage
    message.setFrom(mailFrom)
    message.setTo(mailTo)
    message.setSubject(subject)
    message.setText(text)
    mailSender.send(message)
  }

  @PreDestroy
  def onExit(): Unit = {
    val hostname = InetAddress.getLocalHost.getHostName
    send(s"$hostname $applicationName - Server stopped", "ok?")
  }
}
