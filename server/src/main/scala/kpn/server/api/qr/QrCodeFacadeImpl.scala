package kpn.server.api.qr

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kpn.server.api.Api
import org.springframework.stereotype.Component

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Component
class QrCodeFacadeImpl(api: Api) extends QrCodeFacade {

  override def qrCode(message: String): Array[Byte] = {
    api.execute("qr-code", message) {
      val image = new BufferedImage(255, 255, BufferedImage.TYPE_INT_RGB)
      drawQrCode(image, message)
      toPngByteArray(image)
    }
  }

  private def drawQrCode(image: BufferedImage, message: String): Unit = {
    image.createGraphics
    val graphics = image.getGraphics.asInstanceOf[Graphics2D]
    drawBackground(graphics, image.getWidth, image.getHeight)
    val qrCodeWriter = new QRCodeWriter()
    val bitMatrix = qrCodeWriter.encode(message, BarcodeFormat.QR_CODE, image.getWidth, image.getHeight, hints())
    drawMatrix(graphics, bitMatrix)
  }

  private def hints(): java.util.Hashtable[EncodeHintType, Any] = {
    val hints = new java.util.Hashtable[EncodeHintType, Any]
    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M)
    hints.put(EncodeHintType.MARGIN, 0)
    hints
  }


  private def drawBackground(graphics: Graphics2D, width: Int, height: Int): Unit = {
    graphics.setColor(Color.WHITE)
    graphics.fillRect(0, 0, width, height)
  }

  private def drawMatrix(graphics: Graphics2D, bitMatrix: BitMatrix): Unit = {
    graphics.setColor(Color.BLACK)
    for (x <- 0 until bitMatrix.getWidth) {
      for (y <- 0 until bitMatrix.getHeight) {
        if (bitMatrix.get(x, y)) {
          graphics.fillRect(x, y, 1, 1)
        }
      }
    }
  }

  private def toPngByteArray(image: BufferedImage): Array[Byte] = {
    val bos = new ByteArrayOutputStream()
    ImageIO.write(image, "png", bos)
    bos.toByteArray
  }
}
