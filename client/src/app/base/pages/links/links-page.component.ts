import {Component, OnInit} from "@angular/core";
import {PageService} from "../../../components/shared/page.service";

@Component({
  selector: "kpn-links-page",
  template: `
    <h1>
      Links
    </h1>
  `
})
export class LinksPageComponent implements OnInit {

  constructor(private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
  }

}
