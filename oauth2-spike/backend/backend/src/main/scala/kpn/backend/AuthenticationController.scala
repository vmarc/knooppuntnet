package kpn.backend

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(oauthClientId: String) {

  @GetMapping(value = Array("/api/hello"))
  def hello(): String = {
    "hello"
  }

  @GetMapping(value = Array("/api/client-id"))
  def clientId(): String = {
    oauthClientId
  }

  @GetMapping(value = Array("/api/user"))
  def user(): String = {
    "user"
  }
}
