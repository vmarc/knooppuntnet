package kpn.core.mail

import java.util.Date
import java.util.Properties

import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class MailImpl(config: MailConfig) extends Mail {

  def send(subject: String, text: String): Unit = {

    val props = loadProperties()

    val session = Session.getInstance(props, new Authenticator() {
      override protected def getPasswordAuthentication = new PasswordAuthentication(config.user, config.password)
    })

    try {
      val message = new MimeMessage(session)
      message.setSentDate(new Date)
      message.setFrom(new InternetAddress(config.user))
      message.setRecipient(Message.RecipientType.TO, new InternetAddress(config.recipient))
      message.setSubject(subject)
      message.setText(text)
      Transport.send(message, config.user, config.password)
    } catch {
      case e: MessagingException =>
        throw new RuntimeException(e)
    }
  }

  private def loadProperties(): Properties = {
    val props = new Properties()
    props.put("mail.smtp.host", config.host)
    props.put("mail.smtp.socketFactory.port", config.port)
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
    props.put("mail.smtp.auth", "true")
    props.put("mail.debug", config.debug.toString)
    props
  }

}
