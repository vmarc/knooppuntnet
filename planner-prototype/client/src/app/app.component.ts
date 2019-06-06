import {Component, OnInit} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})


export class AppComponent implements OnInit {

  defaultLanguage = "nl";

  constructor(
    private translate: TranslateService) {
    this.configureLanguages();
  }

  ngOnInit() {
  }

  private configureLanguages() {
    this.translate.addLangs(['en', 'nl']);
    this.translate.setDefaultLang(this.defaultLanguage);

    if (localStorage.getItem("lang") == null) {
      localStorage.setItem("lang", this.defaultLanguage);
    } else {
      this.translate.use(localStorage.getItem("lang"));
    }
  }
}
