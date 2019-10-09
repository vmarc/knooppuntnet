package kpn.core.mail

case class MailConfig(
  host: String,
  port: String,
  user: String,
  password: String,
  recipient: String,
  debug: Boolean
)
