import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { ChangeLocationAnalysisSummaryComponent } from '@app/analysis/components/change-set/change-location-analysis-summary.component';
import { ChangeNetworkAnalysisSummaryComponent } from '@app/analysis/components/change-set/change-network-analysis-summary.component';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { ItemComponent } from '@app/shared/components/items/item.component';
import { ItemsComponent } from '@app/shared/components/items/items.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { NzDividerComponent } from 'ng-zorro-antd/divider';
import { RouterService } from '@app/shared/services/router.service';
import { UserLinkLoginComponent } from '@app/shared/user/user-link-login.component';
import { ChangesComponent } from '../../components/changes/changes.component';
import { ChangesPageService } from './changes-page.service';
import { ChangesSidebarComponent } from './components/changes-sidebar.component';

@Component({
  selector: 'ui-changes-page',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <ui-page>
      <ui-changes-sidebar />
      <nz-divider />
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
      <ui-page-header subject="changes-page" i18n="@@changes-page.title"> Changes </ui-page-header>

      <ui-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!service.loggedIn()) {
            <p i18n="@@changes-page.login-required">
              The details of the changes history are available to logged in OpenStreetMap
              contributors only.
            </p>
            <p>
              <ui-user-link-login />
            </p>
          } @else {
            @if (response.result; as page) {
              <p>
                <ui-situation-on [timestamp]="response.situationOn" />
              </p>
              <ui-changes
                [impact]="service.impact()"
                [pageSize]="service.pageSize()"
                [pageIndex]="service.pageIndex()"
                (impactChange)="onImpactChange($event)"
                (pageSizeChange)="onPageSizeChange($event)"
                (pageIndexChange)="onPageIndexChange($event)"
                [totalCount]="page.changeCount"
                [changeCount]="page.changes.length"
              >
                <ui-items>
                  @for (changeSet of page.changes; track $index) {
                    <ui-item [index]="changeSet.rowIndex">
                      @if (changeSet.network) {
                        <ui-change-network-analysis-summary [changeSet]="changeSet" />
                      }
                      @if (changeSet.location) {
                        <ui-change-location-analysis-summary [changeSet]="changeSet" />
                      }
                    </ui-item>
                  }
                </ui-items>
              </ui-changes>
            }
          }
        </div>
      }
    </ui-page>
  `,
  providers: [ChangesPageService, AnalysisStrategyService, RouterService],
  imports: [
    BreadcrumbComponent,
    ChangeLocationAnalysisSummaryComponent,
    ChangeNetworkAnalysisSummaryComponent,
    ChangesComponent,
    ChangesSidebarComponent,
    ErrorComponent,
    ItemComponent,
    ItemsComponent,
    NzDividerComponent,
    PageComponent,
    PageHeaderComponent,
    SituationOnComponent,
    UserLinkLoginComponent,
  ],
})
export class ChangesPageComponent implements OnInit {
  protected readonly service = inject(ChangesPageService);

  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    Breadcrumbs.analysis,
    { label: Breadcrumbs.changesLabel },
  ];

  ngOnInit(): void {
    this.service.onInit();
  }

  onImpactChange(impact: boolean): void {
    this.service.updateImpact(impact);
  }

  onPageSizeChange(pageSize: number): void {
    this.service.updatePageSize(pageSize);
  }

  onPageIndexChange(pageIndex: number): void {
    this.service.updatePageIndex(pageIndex);
  }
}
