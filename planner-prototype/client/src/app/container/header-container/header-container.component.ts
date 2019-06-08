import {Component} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: "app-header-container",
  templateUrl: "./header-container.component.html",
  styleUrls: ["./header-container.component.scss"]
})
export class HeaderContainerComponent {

  constructor(private translate: TranslateService) {
  }

  useLanguage(language: string) {
    localStorage.setItem("lang", language);
    this.translate.use(language);
  }

}
