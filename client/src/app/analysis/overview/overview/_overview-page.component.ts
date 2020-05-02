import {ChangeDetectionStrategy} from "@angular/core";
import {Component, OnInit} from "@angular/core";
import {Observable} from "rxjs";
import {tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {PageWidthService} from "../../../components/shared/page-width.service";
import {PageService} from "../../../components/shared/page.service";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {Statistics} from "../../../kpn/api/custom/statistics";

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

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
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
export class OverviewPageComponent implements OnInit {

  response$: Observable<ApiResponse<Statistics>>;
  stats: Statistics;

  constructor(private appService: AppService,
              private pageService: PageService,
              private pageWidthService: PageWidthService) {
  }

  ngOnInit(): void {
    this.pageService.defaultMenu();
    this.response$ = this.appService.overview().pipe(
      tap(response => this.stats = response.result)
    );
  }

  isVeryLarge(): boolean {
    return this.pageWidthService.isVeryLarge();
  }
}
