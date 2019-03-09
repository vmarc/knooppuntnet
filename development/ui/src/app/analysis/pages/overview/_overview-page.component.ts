import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {Statistics} from "../../../kpn/shared/statistics/statistics";
import {ApiResponse} from "../../../kpn/shared/api-response";

@Component({
  selector: 'kpn-overview-page',
  template: `

    <div>
      <a routerLink="/">Home</a> >
      <a routerLink="/analysis">Analysis</a> >
      Overview
    </div>

    <h1>
      Overview
    </h1>

    <kpn-statistic-configurations></kpn-statistic-configurations>

    <div *ngIf="response">

      Situation on:
      <kpn-timestamp [timestamp]="response.situationOn"></kpn-timestamp>

      <kpn-overview-table [statistics]="stats"></kpn-overview-table>

      <json [object]="response"></json>
    </div>
  `
})
export class OverviewPageComponent implements OnInit {

  response: ApiResponse<Statistics>;
  stats: Statistics;

  constructor(private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
    this.appService.overview().subscribe(response => {
      this.response = response;
      this.stats = response.result;
    });
  }

}
