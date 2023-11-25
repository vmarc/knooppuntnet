package kpn.backend

import org.apache.tomcat.util.codec.binary.Base64.decodeBase64
import org.apache.tomcat.util.codec.binary.Base64.encodeBase64URLSafeString
import org.springframework.stereotype.Component

import java.nio.charset.StandardCharsets.UTF_8
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Component
class CryptoImpl(cryptoKey: String) extends Crypto {

  private val TRANSFORM = "AES/CBC/PKCS5Padding"
  private val secretKey = new SecretKeySpec(decodeBase64(cryptoKey), "AES")
  private val ivKey = {
    val keyGen = KeyGenerator.getInstance("AES")
    keyGen.init(128)
    new IvParameterSpec(keyGen.generateKey.getEncoded)
  }

  override def encrypt(toEncrypt: String): String = {
    val encipher = Cipher.getInstance(TRANSFORM)
    encipher.init(Cipher.ENCRYPT_MODE, secretKey, ivKey)
    encodeBase64URLSafeString(encipher.doFinal(toEncrypt.getBytes(UTF_8)))
  }

  override def decrypt(toDecrypt: String): String = {
    val decipher = Cipher.getInstance(TRANSFORM)
    decipher.init(Cipher.DECRYPT_MODE, secretKey, ivKey)
    new String(decipher.doFinal(decodeBase64(toDecrypt)), UTF_8)
  }
}
