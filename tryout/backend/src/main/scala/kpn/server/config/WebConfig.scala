package kpn.server.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig extends WebMvcConfigurer {

  override def extendMessageConverters(converters: java.util.List[HttpMessageConverter[_]]): Unit = {
    converters.add(0, new BsonHttpMessageConverter())
  }
}
