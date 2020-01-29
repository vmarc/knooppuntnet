import {Component, OnInit} from "@angular/core";
import {PageService} from "../../../components/shared/page.service";

@Component({
  selector: "kpn-not-found-page",
  template: `
    <h1 i18n="@@not-found.title">
      Not found
    </h1>
  `
})
export class NotFoundPageComponent implements OnInit {

  constructor(private pageService: PageService) {
  }

  ngOnInit(): void {
    this.pageService.defaultMenu();
  }

}
