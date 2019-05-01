import {Component, OnInit} from "@angular/core";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {Statistics} from "../../../kpn/shared/statistics/statistics";

@Component({
  selector: "kpn-overview-page",
  template: `

    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a> >
      <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a> >
      <span i18n="@@breadcrumb.overview">Overview</span>
    </div>

    <h1 i18n="@@overview-page.title">
      Overview
    </h1>

    <kpn-statistic-configurations></kpn-statistic-configurations>

    <div *ngIf="response">
      <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
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
