package kpn.backend

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController {
  @GetMapping(value = Array("/api/hello"))
  def hello(): String = {
    "hello"
  }
}
