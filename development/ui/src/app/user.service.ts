import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";

@Injectable()
export class UserService {

  loginCallbackPage = "";

  constructor(private http: HttpClient,
              private router: Router,
              private cookieService: CookieService) {
  }

  currentUser() {
    return this.cookieService.get("knooppuntnet-user");
  }

  registerLoginCallbackPage(): void {
    if (window.location.pathname === "/login" ||
      window.location.pathname === "/en/login" ||
      window.location.pathname === "/nl/login") {
      this.loginCallbackPage = "";
    }
    else {
      this.loginCallbackPage = window.location.pathname;
    }
  }

  public login(): void {

    let whereWeComeFrom = "";
    if (this.loginCallbackPage.length == 0) {
      whereWeComeFrom = "/";
    }
    else {
      whereWeComeFrom = this.loginCallbackPage;
      this.loginCallbackPage = "";
    }

    const loginUrl = "/api/login?callbackUrl=" + window.location.origin + "/authenticate?page=" + whereWeComeFrom;

    console.log("DEBUG UserService login loginUrl=" + loginUrl);

    // TODO scala version has timeout = 25000

    this.http.get(loginUrl, {
      responseType: 'text'
    }).subscribe(r => {
        console.log("DEBUG UserService success");
        console.log(JSON.stringify(r, null, 2));
        window.location.href = "https://www.openstreetmap.org/oauth/authorize?oauth_token=" + r
      },
      error => {
        console.log("DEBUG UserService error response");
        console.log(JSON.stringify(error, null, 2));
      },
      () => {
        console.log("DEBUG UserService complete");
      });
  }

  public logout(): void {
    this.http.get("/api/logout", {
      responseType: 'text'
    }).subscribe(r => {
        console.log("DEBUG logout success");
        console.log(JSON.stringify(r, null, 2));
      },
      error => {
        console.log("DEBUG logout error response");
        console.log(JSON.stringify(error, null, 2));
      },
      () => {
        console.log("DEBUG logout complete");
      });
  }

  authenticated() {
    let search = decodeURIComponent(window.location.search);
    this.http.get("/api/authenticated" + search, {
      responseType: 'text'
    }).subscribe(r => {
        console.log("DEBUG authenticated success");
        console.log("DEBUG search=" + search);

        const withoutQuestionMark = search.substr(1);
        const firstParam = withoutQuestionMark.split("&")[0];
        const keyAndValue = firstParam.split("=");
        const page = keyAndValue[1].substring(1);
        const pageArray = page.split("/");

        console.log("DEBUG pageArray=" + JSON.stringify(pageArray, null, 2));

        this.router.navigate(pageArray);
      },
      error => {
        console.log("DEBUG authenticated error response");
        console.log(JSON.stringify(error, null, 2));
      },
      () => {
        console.log("DEBUG authenticated complete");
      });
  }

}
