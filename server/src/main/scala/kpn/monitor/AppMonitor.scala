package kpn.monitor

import java.net.InetAddress

import kpn.api.custom.ApiResponse
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.common.TimestampLocal
import kpn.core.common.TimestampUtil
import kpn.core.util.Log
import kpn.server.json.Json
import org.apache.commons.lang3.exception.ExceptionUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class AppMonitor(
  applicationName: String,
  emailSender: JavaMailSender,
  @Value("${mail.from}") mailFrom: String,
  @Value("${mail.to}") mailTo: String,
  @Value("${monitor.frontend.host:localhost}") host: String,
  @Value("${monitor.frontend.port:9005}") port: String,
  @Value("${monitor.alert.minutes}") alertMinutes: Int,
  @Value("${monitor.mail.minutes}") mailMinutes: Int
) {

  private val log = Log(classOf[AppMonitor])
  private var lastEmail: Option[Timestamp] = None

  @Scheduled(initialDelay = 0, fixedDelay = 5 * 60 * 1000)
  def monitor(): Unit = {

    val restTemplate = new RestTemplate
    val url = s"http://$host:$port/api/status/ok"

    try {
      val response: ResponseEntity[String] = restTemplate.getForEntity(url, classOf[String])
      response.getStatusCode match {
        case HttpStatus.OK =>
          val apiResponse = Json.value(response.getBody, classOf[ApiResponse[String]])
          apiResponse.situationOn match {
            case Some(timestamp) =>
              val now = TimestampLocal.toLocal(Time.system())
              val localTimestamp = TimestampLocal.toLocal(timestamp)
              val alertTimestamp = TimestampUtil.relativeSeconds(localTimestamp, alertMinutes * 60)
              if (now > alertTimestamp) {
                throttledSend("alert", localTimestamp.yyyymmddhhmmss)
              }
              else {
                log.info(s"OK - situationOn=${localTimestamp.yyyymmddhhmmss}")
              }
            case _ =>
              throttledSend("alert", "Could not determine situationOn")
          }

        case _ =>
          throttledSend("alert", "Could not retrieve situationOn")
      }
    }
    catch {
      case e: Exception =>
        val stacktrace = ExceptionUtils.getStackTrace(e)
        throttledSend("error", s"Could not retrieve situationOn\n$stacktrace")
    }
  }

  private def throttledSend(subject: String, text: String): Unit = {
    log.info(s"$subject: $text")
    lastEmail match {
      case None =>
        send(subject, text)
        lastEmail = Some(Time.now)
      case Some(lastEmailTimestamp) =>
        val nextEmailTimestamp = TimestampUtil.relativeSeconds(lastEmailTimestamp, mailMinutes * 60)
        if (Time.now > nextEmailTimestamp) {
          send(subject, text)
          lastEmail = Some(Time.now)
        }
    }
  }

  private def send(subject: String, text: String): Unit = {
    log.info(s"SEND $subject: $text")
    val hostname = InetAddress.getLocalHost.getHostName
    val fullSubject = s"$hostname $applicationName - $subject"
    val message = new SimpleMailMessage
    message.setFrom(mailFrom)
    message.setTo(mailTo)
    message.setSubject(fullSubject)
    message.setText(text)
    emailSender.send(message)
  }
}
