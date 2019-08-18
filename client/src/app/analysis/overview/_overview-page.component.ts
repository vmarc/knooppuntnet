import {Component, OnDestroy, OnInit} from "@angular/core";
import {AppService} from "../../app.service";
import {PageWidthService} from "../../components/shared/page-width.service";
import {PageService} from "../../components/shared/page.service";
import {ApiResponse} from "../../kpn/shared/api-response";
import {Statistics} from "../../kpn/shared/statistics/statistics";
import {Subscriptions} from "../../util/Subscriptions";

@Component({
  selector: "kpn-overview-page",
  template: `

    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a> >
      <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a> >
      <span i18n="@@breadcrumb.overview">Overview</span>
    </div>

    <kpn-page-header subject="overview-in-numbers-page" i18n="@@overview-page.title">Overview</kpn-page-header>

    <kpn-statistic-configurations></kpn-statistic-configurations>

    <div *ngIf="response">
      <div class="situation-on">
        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
      </div>

      <kpn-overview-list *ngIf="!isVeryLarge()" [statistics]="stats"></kpn-overview-list>
      <kpn-overview-table *ngIf="isVeryLarge()" [statistics]="stats"></kpn-overview-table>

      <kpn-json [object]="response"></kpn-json>
    </div>
  `,
  styles: [`
    .situation-on {
      padding-bottom: 15px;
    }
  `]
})
export class OverviewPageComponent implements OnInit, OnDestroy {

  response: ApiResponse<Statistics>;
  stats: Statistics;
  private readonly subscriptions = new Subscriptions();

  constructor(private appService: AppService,
              private pageService: PageService,
              private pageWidthService: PageWidthService) {
  }

  ngOnInit(): void {
    this.pageService.defaultMenu();
    this.subscriptions.add(
      this.appService.overview().subscribe(response => {
        this.response = response;
        this.stats = response.result;
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  isVeryLarge(): boolean {
    return this.pageWidthService.isVeryLarge();

  }
}
