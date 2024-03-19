import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ChangesComponent } from '@app/analysis/components/changes';
import { ErrorComponent } from '@app/components/shared/error';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { PageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { RouterService } from '../../../shared/services/router.service';
import { UserLinkLoginComponent } from '../../../shared/user';
import { NodePageHeaderComponent } from '../components/node-page-header.component';
import { NodeChangeComponent } from './components/node-change.component';
import { NodeChangesSidebarComponent } from './components/node-changes-sidebar.component';
import { NodeChangesPageService } from './node-changes-page.service';

@Component({
  selector: 'kpn-node-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a [routerLink]="'/analysis'" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li i18n="@@breadcrumb.node-changes">Node changes</li>
      </ul>

      <kpn-node-page-header pageName="changes" />

      <kpn-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <p i18n="@@node.node-not-found">Node not found</p>
          } @else {
            @if (service.loggedIn() === false) {
              <div>
                <p i18n="@@node.login-required">
                  The details of the node changes history is available to logged in OpenStreetMap
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
                    <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
                  </p>
                  <kpn-changes
                    [impact]="service.impact()"
                    [pageSize]="service.pageSize()"
                    [pageIndex]="service.pageIndex()"
                    (impactChange)="onImpactChange($event)"
                    (pageSizeChange)="onPageSizeChange($event)"
                    (pageIndexChange)="onPageIndexChange($event)"
                    [totalCount]="page.totalCount"
                    [changeCount]="page.changes.length"
                  >
                    <kpn-items>
                      @for (nodeChangeInfo of page.changes; track nodeChangeInfo) {
                        <kpn-item [index]="nodeChangeInfo.rowIndex">
                          <kpn-node-change [nodeChangeInfo]="nodeChangeInfo" />
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
                    <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
                  </p>
                  <kpn-changes
                    [impact]="service.impact()"
                    [pageSize]="service.pageSize()"
                    [pageIndex]="service.pageIndex()"
                    (impactChange)="onImpactChange($event)"
                    (pageSizeChange)="onPageSizeChange($event)"
                    (pageIndexChange)="onPageIndexChange($event)"
                    [totalCount]="page.totalCount"
                    [changeCount]="page.changes.length"
                  >
                    <kpn-items>
                      @for (nodeChangeInfo of page.changes; track nodeChangeInfo) {
                        <kpn-item [index]="nodeChangeInfo.rowIndex">
                          <kpn-node-change [nodeChangeInfo]="nodeChangeInfo" />
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
      <kpn-node-changes-sidebar sidebar />
    </kpn-page>
  `,
  providers: [NodeChangesPageService, RouterService],
  standalone: true,
  imports: [
    ChangesComponent,
    ErrorComponent,
    ItemComponent,
    ItemsComponent,
    NodeChangeComponent,
    NodeChangesSidebarComponent,
    NodePageHeaderComponent,
    PageComponent,
    RouterLink,
    SituationOnComponent,
    UserLinkLoginComponent,
  ],
})
export class NodeChangesPageComponent implements OnInit {
  protected readonly service = inject(NodeChangesPageService);

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
