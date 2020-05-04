package kpn.core.db.mongo

import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistry

class JacksonCodecProvider() extends CodecProvider {
  override def get[T](clazz: Class[T], registry: CodecRegistry): JacksonCodec[T] = {
    new JacksonCodec[T](clazz)
  }
}
