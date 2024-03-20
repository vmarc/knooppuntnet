import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeNetworkAnalysisSummaryComponent } from '@app/analysis/components/change-set';
import { ChangeLocationAnalysisSummaryComponent } from '@app/analysis/components/change-set';
import { ChangesComponent } from '@app/analysis/components/changes';
import { ErrorComponent } from '@app/components/shared/error';
import { ItemsComponent } from '@app/components/shared/items';
import { ItemComponent } from '@app/components/shared/items';
import { PageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { RouterService } from '../../../shared/services/router.service';
import { UserLinkLoginComponent } from '../../../shared/user';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetChangesSidebarComponent } from './components/subset-changes-sidebar.component';
import { SubsetChangesPageService } from './subset-changes-page.service';

@Component({
  selector: 'kpn-subset-changes-page',
  template: `
    <kpn-page>
      <kpn-subset-page-header-block
        pageName="changes"
        pageTitle="Changes"
        i18n-pageTitle="@@subset-changes.title"
      />

      <kpn-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (service.loggedIn() === false) {
            <p i18n="@@subset-changes.login-required">
              This details of the changes history are available to logged in OpenStreetMap
              contributors only.
            </p>
            <p>
              <kpn-user-link-login />
            </p>
          } @else {
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
              [totalCount]="response.result.changeCount"
              [changeCount]="response.result.changes.length"
            >
              <kpn-items>
                @for (changeSet of response.result.changes; track changeSet.rowIndex) {
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
        </div>
      }
      <kpn-subset-changes-sidebar sidebar />
    </kpn-page>
  `,
  providers: [SubsetChangesPageService, RouterService],
  standalone: true,
  imports: [
    ChangeLocationAnalysisSummaryComponent,
    ChangeNetworkAnalysisSummaryComponent,
    ChangesComponent,
    ErrorComponent,
    ItemComponent,
    ItemsComponent,
    PageComponent,
    SituationOnComponent,
    SubsetChangesSidebarComponent,
    SubsetPageHeaderBlockComponent,
    UserLinkLoginComponent,
  ],
})
export class SubsetChangesPageComponent implements OnInit {
  protected readonly service = inject(SubsetChangesPageService);

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
