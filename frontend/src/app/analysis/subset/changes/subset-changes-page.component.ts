import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeLocationAnalysisSummaryComponent } from '@app/analysis/components/change-set';
import { ChangeNetworkAnalysisSummaryComponent } from '@app/analysis/components/change-set';
import { ChangesComponent } from '@app/analysis/components/changes';
import { ErrorComponent } from '@app/components/shared/error';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { PageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { RouterService } from '../../../shared/services/router.service';
import { UserLinkLoginComponent } from '../../../shared/user';
import { UserStore } from '../../../shared/user/user.store';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetChangesSidebarComponent } from './components/subset-changes-sidebar.component';
import { SubsetChangesStore } from './subset-changes.store';

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

      @if (store.response(); as response) {
        <div class="kpn-spacer-above">
          @if (loggedIn() === false) {
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
              [impact]="impact()"
              [pageSize]="pageSize()"
              [pageIndex]="pageIndex()"
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
  providers: [SubsetChangesStore, RouterService],
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
export class SubsetChangesPageComponent {
  private readonly userStore = inject(UserStore);
  protected readonly loggedIn = this.userStore.loggedIn;

  protected readonly store = inject(SubsetChangesStore);
  protected readonly impact = this.store.impact;
  protected readonly pageSize = this.store.pageSize;
  protected readonly pageIndex = this.store.pageIndex;

  onImpactChange(impact: boolean): void {
    this.store.updateImpact(impact);
  }

  onPageSizeChange(pageSize: number): void {
    this.store.updatePageSize(pageSize);
  }

  onPageIndexChange(pageIndex: number): void {
    this.store.updatePageIndex(pageIndex);
  }
}
