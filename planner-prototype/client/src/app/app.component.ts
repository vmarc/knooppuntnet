import {Component} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: "app-root",
  template: `
    <router-outlet></router-outlet>
  `
})
export class AppComponent {

  defaultLanguage = "nl";

  constructor(private translate: TranslateService) {
    this.configureLanguages();
  }

  private configureLanguages() {

    this.translate.addLangs(["en", "nl"]);
    this.translate.setDefaultLang(this.defaultLanguage);

    if (localStorage.getItem("lang") == null) {
      localStorage.setItem("lang", this.defaultLanguage);
    } else {
      this.translate.use(localStorage.getItem("lang"));
    }
  }
}
