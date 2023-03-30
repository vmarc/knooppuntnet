package kpn.server.api.qr

import javax.servlet.http.HttpServletRequest

trait QrCodeFacade {

  def qrCode(request: HttpServletRequest, user: Option[String], url: String): Array[Byte]
}
