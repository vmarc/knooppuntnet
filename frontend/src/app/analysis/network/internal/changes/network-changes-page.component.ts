import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangesComponent } from '@app/analysis/components/changes/changes.component';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter/change-filter.component';
import { NetworkNotFoundComponent } from '@app/analysis/network/internal/components/network-not-found.component';
import { ChangeOption } from '@app/shared/kpn/common/change-option';
import { ItemComponent } from '@app/shared/components/items/item.component';
import { ItemsComponent } from '@app/shared/components/items/items.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { RouterService } from '@app/shared/services/router.service';
import { UserLinkLoginComponent } from '@app/shared/user/user-link-login.component';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { NetworkChangeSetComponent } from './components/network-change-set.component';
import { NetworkChangesPageService } from './network-changes-page.service';

@Component({
  selector: 'ui-network-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-network-page-header
        pageName="changes"
        pageTitle="Changes"
        i18n-pageTitle="@@network-changes.title"
      />

      <ui-change-filter
        [filterOptions]="filterOptions()"
        (optionSelected)="onOptionSelected($event)"
      />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <ui-network-not-found />
          } @else {
            @if (service.loggedIn() === false) {
              <p i18n="@@network-changes.login-required">
                The details of network history are available to logged in OpenStreetMap contributors
                only.
              </p>
              <p>
                <ui-user-link-login />
              </p>
            } @else {
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
                [totalCount]="response.result.totalCount"
                [changeCount]="response.result.changes.length"
              >
                <ui-items>
                  @for (
                    networkChangeInfo of response.result.changes;
                    track networkChangeInfo.rowIndex
                  ) {
                    <ui-item [index]="networkChangeInfo.rowIndex">
                      <ui-network-change-set [networkChangeInfo]="networkChangeInfo" />
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
  providers: [NetworkChangesPageService, RouterService],
  imports: [
    ChangeFilterComponent,
    ChangesComponent,
    ItemComponent,
    ItemsComponent,
    NetworkChangeSetComponent,
    NetworkNotFoundComponent,
    NetworkPageHeaderComponent,
    PageComponent,
    SituationOnComponent,
    UserLinkLoginComponent,
  ],
})
export class NetworkChangesPageComponent implements OnInit {
  protected readonly service = inject(NetworkChangesPageService);
  protected readonly filterOptions = this.service.filterOptions;

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

  onOptionSelected(option: ChangeOption): void {
    this.service.updateFilterOption(option);
  }
}
