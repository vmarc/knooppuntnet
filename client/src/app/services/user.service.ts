import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {BrowserStorageService} from "./browser-storage.service";
import {CookieService} from "ngx-cookie-service";

@Injectable()
export class UserService {

  loginCallbackPage = "";

  constructor(private http: HttpClient,
              private router: Router,
              private browserStorageService: BrowserStorageService) {
  }

  public isLoggedIn(): boolean {
    const xx = !!this.currentUser();
    console.log("isLoggedIn=" + xx);
    return xx;
  }

  public currentUser(): string {
    const user = this.browserStorageService.get("user");
    console.log("currentUser(): user=" + user);
    if (user !== null) {
      return user;
    }
    return "";
  }

  registerLoginCallbackPage(): void {
    if (window.location.pathname === "/login" ||
      window.location.pathname === "/en/login" ||
      window.location.pathname === "/nl/login") {
      this.loginCallbackPage = "";
    } else {
      this.loginCallbackPage = window.location.pathname;
    }
  }

  public login(): void {

    let whereWeComeFrom = "";
    if (this.loginCallbackPage.length === 0) {
      whereWeComeFrom = "/";
    } else {
      whereWeComeFrom = this.loginCallbackPage;
      this.loginCallbackPage = "";
    }

    const loginUrl = "/json-api/login?callbackUrl=" + window.location.origin + "/authenticate?page=" + whereWeComeFrom;

    console.log("DEBUG UserService login loginUrl=" + loginUrl);

    // TODO scala version has timeout = 25000

    this.http.get(loginUrl, {
      responseType: "text"
    }).subscribe(r => {
        console.log("DEBUG UserService success");
        console.log(JSON.stringify(r, null, 2));
        window.location.href = "https://www.openstreetmap.org/oauth/authorize?oauth_token=" + r;
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
    this.browserStorageService.remove("user");
    this.http.get("/json-api/logout", {
      responseType: "text"
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
    const search = decodeURIComponent(window.location.search);
    this.http.get("/json-api/authenticated" + search, {
      responseType: "text"
    }).subscribe(user => {
        this.browserStorageService.set("user", user);
        console.log("DEBUG authenticated success, user=" + user);
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

  private parseJwt(token: string) {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(atob(base64).split("").map(function (c) {
      return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(""));

    return JSON.parse(jsonPayload);
  }
}
