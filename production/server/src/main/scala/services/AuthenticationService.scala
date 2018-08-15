package services

import java.net.URLDecoder
import java.net.URLEncoder

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import kpn.core.user.UserParser
import kpn.core.util.Log
import play.api.libs.oauth.ConsumerKey
import play.api.libs.oauth.OAuth
import play.api.libs.oauth.OAuthCalculator
import play.api.libs.oauth.RequestToken
import play.api.libs.oauth.ServiceInfo
import play.api.libs.ws.WSClient
import play.api.mvc.AnyContent
import play.api.mvc.Cookie
import play.api.mvc.Request
import play.api.mvc.Result
import play.api.mvc.Results

import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class AuthenticationService(
  crypto: Crypto,
  wsClient: WSClient,
  oauthApplicationKey: String,
  oauthApplicationSecret: String
)(implicit val system: ActorSystem) {

  private val log = Log(classOf[AuthenticationService])
  private implicit val executionContext: ExecutionContext = system.dispatcher
  private implicit val materializer: ActorMaterializer = ActorMaterializer()

  private val oauthTokenParameter = "oauth_token"
  private val oauthVerifierParameter = "oauth_verifier"
  private val requestTokenSecretParameter = "requestTokenSecret"
  private val accessTokenParameter = "token"
  private val accessTokenSecretParameter = "secret"
  private val userCookieName = "knooppuntnet-user"

  private val KEY = ConsumerKey(oauthApplicationKey, oauthApplicationSecret)

  private val OAUTH = OAuth(
    ServiceInfo(
      "https://www.openstreetmap.org/oauth/request_token",
      "https://www.openstreetmap.org/oauth/access_token",
      "https://www.openstreetmap.org/oauth/authorize",
      KEY
    ),
    use10a = true
  )

  def retrieveRequestToken(request: Request[AnyContent]): Result = {

    log.elapsed {

      val user = request.session.get("user")

      request.queryString.get("callbackUrl") match {
        case Some(Seq(encodedCallbackUrl)) =>
          val callbackUrl = URLDecoder.decode(encodedCallbackUrl, "UTF-8")
          OAUTH.retrieveRequestToken(callbackUrl) match {
            case Right(requestToken) =>
              val message = "OK"
              val encryptedRequestTokenSecret = crypto.encrypt(requestToken.secret)
              val result = Results.Ok(requestToken.token).withSession(requestTokenSecretParameter -> encryptedRequestTokenSecret)
              (s"$user retrieveRequestToken $message", result)
            case Left(e) =>
              val message = "retrieveRequestToken failed: " + e.getMessage
              val result = Results.BadRequest(message)
              (s"$user retrieveRequestToken $message", result)
          }

        case _ =>
          val message1 = "callbackUrl parameter missing"
          val message2 = s"$message1 ${request.queryString}"
          val result = Results.BadRequest(message1)
          (s"$user retrieveRequestToken $message2", result)
      }
    }
  }

  def retrieveAccessRequestToken(request: Request[AnyContent]): Result = {

    log.elapsed {
      val initialUser = request.session.get("user")

      request.queryString.get(oauthTokenParameter) match {
        case Some(Seq(oauthToken)) =>

          request.queryString.get(oauthVerifierParameter) match {
            case Some(Seq(oauthVerifier)) =>

              request.session.get(requestTokenSecretParameter) match {
                case Some(encryptedRequestTokenSecret) =>

                  val requestTokenSecret = crypto.decrypt(encryptedRequestTokenSecret)
                  val token = RequestToken(oauthToken, requestTokenSecret)
                  OAUTH.retrieveAccessToken(token, oauthVerifier) match {
                    case Right(accessToken) =>

                      val wsRequest = wsClient.url("https://api.openstreetmap.org/api/0.6/user/details").sign(OAuthCalculator(OAUTH.info.key, accessToken))
                      val xxx = wsRequest.get()
                      val yyy = Await.result(xxx, 10.seconds)

                      new UserParser().parse(yyy.xml) match {
                        case Some(user) =>

                          val cookie = Cookie(name = userCookieName,
                            value = URLEncoder.encode(user, "UTF-8"),
                            maxAge = Some(99999999),
                            secure = false, // TODO LOGIN can set to true if we enable SSL in play itself instead of in nginx ???
                            httpOnly = false,
                            sameSite = Some(Cookie.SameSite.Lax)
                          )

                          val accessTokenToken = crypto.encrypt(accessToken.token)
                          val accessTokenSecret = crypto.encrypt(accessToken.secret)

                          val result = Results.Ok(user).withSession("user" -> user, accessTokenParameter -> accessTokenToken, accessTokenSecretParameter
                            -> accessTokenSecret).withCookies(cookie)
                          (s"$user retrieveAccessRequestToken OK", result)

                        case None =>

                          val message1 = "Could not determine username"
                          val message2 = message1 + "\n" + yyy.xml.toString()
                          val result = Results.BadRequest(message1)
                          (s"$initialUser retrieveAccessRequestToken $message2", result)
                      }

                    case Left(e) =>
                      val message = "Error retrieving access token from OpenStreetMap - " + e.getMessage
                      val result = Results.BadRequest(message)
                      (s"$initialUser retrieveAccessRequestToken $message", result)
                  }

                case _ =>
                  val message1 = s"$requestTokenSecretParameter missing in cookie"
                  val message2 = s"$message1 ${request.queryString}"
                  val result = Results.BadRequest(message1)
                  (s"$initialUser retrieveAccessRequestToken $message2", result)
              }

            case _ =>
              val message1 = s"$oauthVerifierParameter request parameter missing"
              val message2 = s"$message1 ${request.queryString}"
              val result = Results.BadRequest(message1)
              (s"$initialUser retrieveAccessRequestToken $message2", result)
          }

        case _ =>
          val message1 = s"$oauthTokenParameter request parameter missing"
          val message2 = s"$message1 ${request.queryString}"
          val result = Results.BadRequest(message1)
          (s"$initialUser retrieveAccessRequestToken $message2", result)
      }
    }
  }

  def logout(request: Request[AnyContent]): Result = {
    val user = request.session.get("user")
    log.infoElapsed(s"$user logout") {
      // destroy the session and the user cookie
      val cookie = Cookie(
        name = userCookieName,
        value = "",
        maxAge = Some(-1), // expire now
        httpOnly = false,
        sameSite = Some(Cookie.SameSite.Lax)
      )
      Results.Ok("").withSession().withCookies(cookie)
    }
  }

}
