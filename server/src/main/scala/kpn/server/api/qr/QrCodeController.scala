package kpn.server.api.qr

import kpn.server.api.CurrentUser
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class QrCodeController(qrCodeFacade: QrCodeFacade) {
  @ResponseBody
  @PostMapping(
    path = Array("/api/qr-code"),
    consumes = Array("text/plain"),
    produces = Array(MediaType.IMAGE_PNG_VALUE)
  )
  def qrCode(@RequestBody url: String): Array[Byte] = {
    qrCodeFacade.qrCode(CurrentUser.name, url)
  }
}
