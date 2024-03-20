import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ChangesComponent } from '@app/analysis/components/changes';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { PageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { RouterService } from '../../../shared/services/router.service';
import { UserLinkLoginComponent } from '../../../shared/user';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';
import { RouteChangeComponent } from './components/route-change.component';
import { RouteChangesSidebarComponent } from './components/route-changes-sidebar.component';
import { RouteChangesPageService } from './route-changes-page.service';

@Component({
  selector: 'kpn-route-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a [routerLink]="'/analysis'" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li i18n="@@breadcrumb.route-changes">Route changes</li>
      </ul>

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
      <kpn-route-changes-sidebar sidebar />
    </kpn-page>
  `,
  providers: [RouteChangesPageService, RouterService],
  standalone: true,
  imports: [
    AsyncPipe,
    ChangesComponent,
    ItemComponent,
    ItemsComponent,
    PageComponent,
    RouteChangeComponent,
    RouteChangesSidebarComponent,
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
}
