import {Component, OnInit} from '@angular/core';
import {PageService} from "../../../shared/page.service";

@Component({
  selector: 'kpn-glossary-page',
  template: `
    <h1>
      Glossary
    </h1>
  `
})
export class GlossaryPageComponent implements OnInit {

  constructor(private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
  }

}
