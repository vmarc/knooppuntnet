import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { ChangeNetworkAnalysisSummaryComponent } from '@app/analysis/components/change-set/change-network-analysis-summary.component';
import { ChangeLocationAnalysisSummaryComponent } from '@app/analysis/components/change-set/change-location-analysis-summary.component';
import { ChangesComponent } from '@app/analysis/components/changes/changes.component';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter/change-filter.component';
import { ChangeOption } from '@app/shared/kpn/common/change-option';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { ItemComponent } from '@app/shared/components/items/item.component';
import { ItemsComponent } from '@app/shared/components/items/items.component';
import { PageFilterComponent } from '@app/shared/components/page/page-filter.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { RouterService } from '@app/shared/services/router.service';
import { UserLinkLoginComponent } from '@app/shared/user/user-link-login.component';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetChangesPageService } from './subset-changes-page.service';

@Component({
  selector: 'ui-subset-changes-page',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <ui-page-filter>
      <ui-change-filter
        [filterOptions]="filterOptions()"
        (optionSelected)="onOptionSelected($event)"
        filter
      />
      <ui-subset-page-header-block
        pageName="changes"
        pageTitle="Changes"
        i18n-pageTitle="@@subset-changes.title"
      />

      <ui-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (service.loggedIn() === false) {
            <p i18n="@@subset-changes.login-required">
              This details of the changes history are available to logged in OpenStreetMap
              contributors only.
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
              [totalCount]="response.result.changeCount"
              [changeCount]="response.result.changes.length"
            >
              <ui-items>
                @for (changeSet of response.result.changes; track changeSet.rowIndex) {
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
        </div>
      }
    </ui-page-filter>
  `,
  providers: [SubsetChangesPageService, RouterService],
  imports: [
    ChangeFilterComponent,
    ChangeLocationAnalysisSummaryComponent,
    ChangeNetworkAnalysisSummaryComponent,
    ChangesComponent,
    ErrorComponent,
    ItemComponent,
    ItemsComponent,
    PageFilterComponent,
    SituationOnComponent,
    SubsetPageHeaderBlockComponent,
    UserLinkLoginComponent,
  ],
})
export class SubsetChangesPageComponent implements OnInit {
  protected readonly service = inject(SubsetChangesPageService);
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
    this.service.setPageIndex(pageIndex);
  }

  onOptionSelected(option: ChangeOption): void {
    this.service.setFilterOption(option);
  }
}
