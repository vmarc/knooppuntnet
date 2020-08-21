package kpn.core.tools.support

import java.io.File
import java.io.FileReader
import java.util.Properties

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSenderImpl

class ToolMailSender {

  private val mailSender = new JavaMailSenderImpl()

  private val properties = new Properties()
  properties.load(new FileReader(new File("/kpn/conf/server.properties")))
  mailSender.setHost(properties.getProperty("spring.mail.host"))
  mailSender.setPort(properties.getProperty("spring.mail.port").toInt)
  mailSender.setUsername(properties.getProperty("spring.mail.username"))
  mailSender.setPassword(properties.getProperty("spring.mail.password"))

  private val props = mailSender.getJavaMailProperties
  props.put("mail.smtp.auth", properties.getProperty("spring.mail.properties.mail.smtp.auth"))
  props.put("mail.smtp.starttls.enable", properties.getProperty("spring.mail.properties.mail.smtp.starttls.enable"))
  props.put("mail.debug", properties.getProperty("spring.mail.properties.mail.debug"))

  private val mailFrom = properties.getProperty("mail.from")
  private val mailTo = properties.getProperty("mail.to")

  def send(subject: String, text: String): Unit = {
    val message = new SimpleMailMessage
    message.setFrom(mailFrom)
    message.setTo(mailTo)
    message.setSubject(subject)
    message.setText(text)
    mailSender.send(message)
  }

}
