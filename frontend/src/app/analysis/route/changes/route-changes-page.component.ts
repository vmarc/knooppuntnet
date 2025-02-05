import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ChangesComponent } from '@app/analysis/components/changes';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter';
import { ChangeOption } from '@app/kpn/common';
import { ItemComponent } from '@app/shared/components/items/item.component';
import { ItemsComponent } from '@app/shared/components/items/items.component';
import { PageFilterComponent } from '@app/shared/components/page/page-filter.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { RouterService } from '../../../shared/services/router.service';
import { UserLinkLoginComponent } from '../../../shared/user';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';
import { RouteChangeComponent } from './components/route-change.component';
import { RouteChangesPageService } from './route-changes-page.service';

@Component({
  selector: 'kpn-route-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-filter>
      <nz-breadcrumb>
        <nz-breadcrumb-item>
          <a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <a [routerLink]="'/analysis'" i18n="@@breadcrumb.analysis">Analysis</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <span i18n="@@breadcrumb.route-changes">Route changes</span>
        </nz-breadcrumb-item>
      </nz-breadcrumb>

      <kpn-route-page-header pageName="changes" />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <div i18n="@@route.route-not-found">Route not found</div>
          } @else {
            @if (service.loggedIn() === false) {
              <div>
                <p i18n="@@route-changes.login-required">
                  The details of the route history is available to logged in OpenStreetMap
                  contributors only.
                </p>
                <p>
                  <kpn-user-link-login />
                </p>
              </div>
            } @else {
              @if (response.result; as page) {
                <div>
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
                    [totalCount]="page.totalCount"
                    [changeCount]="page.changeCount"
                  >
                    <kpn-items>
                      @for (routeChangeInfo of page.changes; track routeChangeInfo) {
                        <kpn-item [index]="routeChangeInfo.rowIndex">
                          <kpn-route-change [routeChangeInfo]="routeChangeInfo" />
                        </kpn-item>
                      }
                    </kpn-items>
                  </kpn-changes>
                </div>
              }
            }
            <ng-template #changes>
              @if (response.result; as page) {
                <div>
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
                    [totalCount]="page.totalCount"
                    [changeCount]="page.changeCount"
                  >
                    <kpn-items>
                      @for (routeChangeInfo of page.changes; track routeChangeInfo) {
                        <kpn-item [index]="routeChangeInfo.rowIndex">
                          <kpn-route-change [routeChangeInfo]="routeChangeInfo" />
                        </kpn-item>
                      }
                    </kpn-items>
                  </kpn-changes>
                </div>
              }
            </ng-template>
          }
        </div>
      }
      <kpn-change-filter
        [filterOptions]="service.filterOptions()"
        (optionSelected)="onOptionSelected($event)"
        filter
      />
    </kpn-page-filter>
  `,
  providers: [RouteChangesPageService, RouterService],
  imports: [
    ChangeFilterComponent,
    ChangesComponent,
    ItemComponent,
    ItemsComponent,
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
    PageFilterComponent,
    RouteChangeComponent,
    RoutePageHeaderComponent,
    RouterLink,
    SituationOnComponent,
    UserLinkLoginComponent,
  ],
})
export class RouteChangesPageComponent implements OnInit {
  protected readonly service = inject(RouteChangesPageService);

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
