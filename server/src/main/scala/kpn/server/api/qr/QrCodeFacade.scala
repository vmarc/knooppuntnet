package kpn.server.api.qr

trait QrCodeFacade {

  def qrCode(user: Option[String], url: String): Array[Byte]
}
