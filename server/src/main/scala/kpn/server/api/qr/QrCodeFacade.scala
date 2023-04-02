package kpn.server.api.qr

trait QrCodeFacade {

  def qrCode(url: String): Array[Byte]
}
