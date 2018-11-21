import {Component, OnInit} from '@angular/core';
import {PageService} from "../../shared/page.service";

@Component({
  selector: 'kpn-about-page',
  template: `
    <h1>
      About
    </h1>
  `
})
export class AboutPageComponent implements OnInit {

  constructor(private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
  }

}
