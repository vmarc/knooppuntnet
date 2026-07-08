package kpn.server.config

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("dev & web", "dev & analysis"))
class MailerNull extends Mailer {

  override def send(subject: String, text: String): Unit = {
    // do nothing
  }
}
