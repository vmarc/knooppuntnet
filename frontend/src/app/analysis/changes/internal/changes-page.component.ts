import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ChangeLocationAnalysisSummaryComponent } from '@app/analysis/components/change-set/change-location-analysis-summary.component';
import { ChangeNetworkAnalysisSummaryComponent } from '@app/analysis/components/change-set/change-network-analysis-summary.component';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { ItemComponent } from '@app/shared/components/items/item.component';
import { ItemsComponent } from '@app/shared/components/items/items.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { NzDividerComponent } from 'ng-zorro-antd/divider';
import { RouterService } from '@app/shared/services/router.service';
import { UserLinkLoginComponent } from '@app/shared/user/user-link-login.component';
import { ChangesComponent } from '../../components/changes/changes.component';
import { ChangesPageService } from './changes-page.service';
import { ChangesSidebarComponent } from './components/changes-sidebar.component';

@Component({
  selector: 'kpn-changes-page',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <kpn-page>
      <kpn-changes-sidebar />
      <nz-divider />
      <nz-breadcrumb>
        <nz-breadcrumb-item>
          <a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <a [routerLink]="'/analysis'" i18n="@@breadcrumb.analysis">Analysis</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <span i18n="@@breadcrumb.changes">Changes</span>
        </nz-breadcrumb-item>
      </nz-breadcrumb>

      <kpn-page-header subject="changes-page" i18n="@@changes-page.title">
        Changes
      </kpn-page-header>

      <kpn-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!service.loggedIn()) {
            <p i18n="@@changes-page.login-required">
              The details of the changes history are available to logged in OpenStreetMap
              contributors only.
            </p>
            <p>
              <kpn-user-link-login />
            </p>
          } @else {
            @if (response.result; as page) {
              <p>
                <kpn-situation-on [timestamp]="response.situationOn" />
              </p>
              <kpn-changes
                [impact]="service.impact()"
                [pageSize]="service.pageSize()"
                [pageIndex]="service.pageIndex()"
                (impactChange)="onImpactChange($event)"
                (pageSizeChange)="onPageSizeChange($event)"
                (pageIndexChange)="onPageIndexChange($event)"
                [totalCount]="page.changeCount"
                [changeCount]="page.changes.length"
              >
                <kpn-items>
                  @for (changeSet of page.changes; track $index) {
                    <kpn-item [index]="changeSet.rowIndex">
                      @if (changeSet.network) {
                        <kpn-change-network-analysis-summary [changeSet]="changeSet" />
                      }
                      @if (changeSet.location) {
                        <kpn-change-location-analysis-summary [changeSet]="changeSet" />
                      }
                    </kpn-item>
                  }
                </kpn-items>
              </kpn-changes>
            }
          }
        </div>
      }
    </kpn-page>
  `,
  providers: [ChangesPageService, AnalysisStrategyService, RouterService],
  imports: [
    ChangeLocationAnalysisSummaryComponent,
    ChangeNetworkAnalysisSummaryComponent,
    ChangesComponent,
    ChangesSidebarComponent,
    ErrorComponent,
    ItemComponent,
    ItemsComponent,
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
    NzDividerComponent,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
    SituationOnComponent,
    UserLinkLoginComponent,
  ],
})
export class ChangesPageComponent implements OnInit {
  protected readonly service = inject(ChangesPageService);

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
