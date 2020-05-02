import {ChangeDetectionStrategy} from "@angular/core";
import {Component, OnDestroy, OnInit} from "@angular/core";
import {AppService} from "../../../app.service";
import {PageWidthService} from "../../../components/shared/page-width.service";
import {PageService} from "../../../components/shared/page.service";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {Statistics} from "../../../kpn/api/custom/statistics";
import {Subscriptions} from "../../../util/Subscriptions";

@Component({
  selector: "kpn-overview-page",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a></li>
      <li i18n="@@breadcrumb.overview">Overview</li>
    </ul>

    <kpn-page-header subject="overview-in-numbers-page" i18n="@@overview-page.title">Overview</kpn-page-header>

    <kpn-statistic-configurations></kpn-statistic-configurations>

    <div *ngIf="response" class="kpn-spacer-above">
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
