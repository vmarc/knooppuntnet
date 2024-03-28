import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { PageHeaderComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { OverviewPageService } from './overview-page.service';
import { OverviewListComponent } from './components/overview-list.component';
import { OverviewPageBreadcrumbComponent } from './components/overview-page-breadcrumb.component';
import { OverviewSidebarComponent } from './components/overview-sidebar.component';
import { OverviewTableComponent } from './components/overview-table.component';

@Component({
  selector: 'kpn-overview-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-overview-page-breadcrumb />

      <kpn-page-header subject="overview-in-numbers-page" i18n="@@overview-page.title">
        Overview
      </kpn-page-header>

      <kpn-error></kpn-error>

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (response.result) {
            <div class="kpn-small-spacer-below">
              <kpn-situation-on [timestamp]="response.situationOn" />
            </div>
            @if (service.tableFormat()) {
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
  standalone: true,
  imports: [
    ErrorComponent,
    OverviewListComponent,
    OverviewSidebarComponent,
    OverviewTableComponent,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
    SituationOnComponent,
    OverviewPageBreadcrumbComponent,
  ],
})
export class OverviewPageComponent implements OnInit {
  protected readonly service = inject(OverviewPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
