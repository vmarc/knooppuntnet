import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../app.service";
import {PageService} from "../../../shared/page.service";

@Component({
  selector: 'kpn-overview-page',
  template: `
    <h1>
      Overview
    </h1>
    <div *ngIf="response">
      <json [object]="response"></json>
    </div>
  `
})
export class OverviewPageComponent implements OnInit {

  response: any;

  constructor(private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
    this.appService.overview().subscribe(response => {
      this.response = response;
    });
  }

}
