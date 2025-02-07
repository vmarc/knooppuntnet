import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { OverviewPageService } from './overview-page.service';
import { OverviewListComponent } from './components/overview-list.component';
import { OverviewPageBreadcrumbComponent } from './components/overview-page-breadcrumb.component';
import { OverviewOptionsComponent } from './components/overview-options.component';
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

      <kpn-overview-options />

      <kpn-error />

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
    </kpn-page>
  `,
  imports: [
    ErrorComponent,
    OverviewListComponent,
    OverviewPageBreadcrumbComponent,
    OverviewOptionsComponent,
    OverviewTableComponent,
    PageComponent,
    PageHeaderComponent,
    SituationOnComponent,
  ],
})
export class OverviewPageComponent implements OnInit {
  protected readonly service = inject(OverviewPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
