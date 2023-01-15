import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { StatisticValues } from '@api/common/statistics/statistic-values';
import { ApiResponse } from '@api/custom/api-response';
import { Observable } from 'rxjs';
import { AppService } from '../../../app.service';
import { OverviewService } from '../overview.service';

@Component({
  selector: 'kpn-overview-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li i18n="@@breadcrumb.overview">Overview</li>
    </ul>

    <kpn-page-header
      subject="overview-in-numbers-page"
      i18n="@@overview-page.title"
    >Overview
    </kpn-page-header>

    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="response.result">
        <div class="situation-on">
          <kpn-situation-on [timestamp]="response.situationOn" />
        </div>
        <ng-content
          *ngIf="tableFormat$ | async; then table; else list"
        ></ng-content>
        <ng-template #table>
          <kpn-overview-table [statistics]="response.result" />
        </ng-template>
        <ng-template #list>
          <kpn-overview-list [statistics]="response.result"/>
        </ng-template>
      </div>
    </div>
  `,
  styles: [
    `
      .situation-on {
        padding-bottom: 15px;
      }
    `,
  ],
})
export class OverviewPageComponent implements OnInit {
  readonly tableFormat$ = this.overviewService.tableFormat$;
  response$: Observable<ApiResponse<StatisticValues[]>>;

  constructor(
    private appService: AppService,
    private overviewService: OverviewService
  ) {}

  ngOnInit(): void {
    this.response$ = this.appService.overview();
  }
}
