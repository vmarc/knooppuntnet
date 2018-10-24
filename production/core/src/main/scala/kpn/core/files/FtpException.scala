package kpn.core.files

import java.io.IOException

class FtpException(val message: String, val replyStrings: Array[String]) extends IOException(message) {
  override def getMessage: String = super.getMessage + ": " + replyStrings.mkString(",")
}
