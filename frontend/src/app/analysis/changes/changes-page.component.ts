import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ChangeLocationAnalysisSummaryComponent } from '@app/analysis/components/change-set';
import { ChangeNetworkAnalysisSummaryComponent } from '@app/analysis/components/change-set';
import { ChangesComponent } from '@app/analysis/components/changes';
import { ErrorComponent } from '@app/components/shared/error';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { PageComponent } from '@app/components/shared/page';
import { PageHeaderComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { RouterService } from '../../shared/services/router.service';
import { UserLinkLoginComponent } from '../../shared/user';
import { ChangesPageService } from './changes-page.service';
import { ChangesSidebarComponent } from './components/changes-sidebar.component';

@Component({
  selector: 'kpn-changes-page',
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a [routerLink]="'/analysis'" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li i18n="@@breadcrumb.changes">Changes</li>
      </ul>

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
      <kpn-changes-sidebar sidebar />
    </kpn-page>
  `,
  providers: [ChangesPageService, RouterService],
  standalone: true,
  imports: [
    ChangeLocationAnalysisSummaryComponent,
    ChangeNetworkAnalysisSummaryComponent,
    ChangesComponent,
    ChangesSidebarComponent,
    ErrorComponent,
    ItemComponent,
    ItemsComponent,
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
    this.service.setImpact(impact);
  }

  onPageSizeChange(pageSize: number): void {
    this.service.setPageSize(pageSize);
  }

  onPageIndexChange(pageIndex: number): void {
    this.service.setPageIndex(pageIndex);
  }
}
