import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { OverviewPageService } from './overview-page.service';
import { OverviewListComponent } from './components/overview-list.component';
import { OverviewOptionsComponent } from './components/overview-options.component';
import { OverviewTableComponent } from './components/overview-table.component';

@Component({
  selector: 'ui-overview-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />

      <ui-page-header subject="overview-in-numbers-page" i18n="@@overview-page.title">
        Overview
      </ui-page-header>

      <ui-overview-options />

      <ui-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (response.result) {
            <div class="kpn-small-spacer-below">
              <ui-situation-on [timestamp]="response.situationOn" />
            </div>
            @if (service.tableFormat()) {
              <ui-overview-table [statistics]="response.result" />
            } @else {
              <ui-overview-list [statistics]="response.result" />
            }
          }
        </div>
      }
    </ui-page>
  `,
  imports: [
    ErrorComponent,
    OverviewListComponent,
    OverviewOptionsComponent,
    OverviewTableComponent,
    PageComponent,
    PageHeaderComponent,
    SituationOnComponent,
    BreadcrumbComponent,
  ],
})
export class OverviewPageComponent implements OnInit {
  protected readonly service = inject(OverviewPageService);

  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    Breadcrumbs.analysis,
    { label: Breadcrumbs.overviewLabel },
  ];

  ngOnInit(): void {
    this.service.onInit();
  }
}
