import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { StatisticValues } from '@api/common/statistics';
import { ApiResponse } from '@api/custom';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { PageHeaderComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { ApiService } from '@app/services';
import { Observable } from 'rxjs';
import { OverviewService } from '../overview.service';
import { OverviewListComponent } from './components/overview-list.component';
import { OverviewSidebarComponent } from './components/overview-sidebar.component';
import { OverviewTableComponent } from './components/overview-table.component';

@Component({
  selector: 'kpn-overview-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li i18n="@@breadcrumb.overview">Overview</li>
      </ul>

      <kpn-page-header subject="overview-in-numbers-page" i18n="@@overview-page.title"
        >Overview
      </kpn-page-header>

      <kpn-error></kpn-error>

      @if (response$ | async; as response) {
        <div class="kpn-spacer-above">
          @if (response.result) {
            <div class="situation-on">
              <kpn-situation-on [timestamp]="response.situationOn" />
            </div>
            @if (tableFormat()) {
              <kpn-overview-table [statistics]="response.result" />
            } @else {
              <kpn-overview-list [statistics]="response.result" />
            }
          }
        </div>
      }
      <kpn-overview-sidebar sidebar />
    </kpn-page>
  `,
  styles: `
    .situation-on {
      padding-bottom: 15px;
    }
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    ErrorComponent,
    OverviewListComponent,
    OverviewSidebarComponent,
    OverviewTableComponent,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
    SituationOnComponent,
  ],
})
export class OverviewPageComponent implements OnInit {
  private readonly apiService = inject(ApiService);
  private readonly overviewService = inject(OverviewService);

  protected readonly tableFormat = this.overviewService.tableFormat;
  protected response$: Observable<ApiResponse<StatisticValues[]>>;

  ngOnInit(): void {
    this.response$ = this.apiService.overview();
  }
}
