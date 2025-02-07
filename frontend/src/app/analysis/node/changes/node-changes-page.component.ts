import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ChangesComponent } from '@app/analysis/components/changes';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { ItemComponent } from '@app/shared/components/items/item.component';
import { ItemsComponent } from '@app/shared/components/items/items.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { RouterService } from '../../../shared/services/router.service';
import { UserLinkLoginComponent } from '../../../shared/user';
import { NodePageHeaderComponent } from '../components/node-page-header.component';
import { NodeChangeComponent } from './components/node-change.component';
import { NodeChangesPageService } from './node-changes-page.service';

@Component({
  selector: 'kpn-node-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <nz-breadcrumb>
        <nz-breadcrumb-item>
          <a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <a [routerLink]="'/analysis'" i18n="@@breadcrumb.analysis">Analysis</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <span i18n="@@breadcrumb.node-changes">Node changes</span>
        </nz-breadcrumb-item>
      </nz-breadcrumb>

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
    </kpn-page>
  `,
  providers: [NodeChangesPageService, RouterService],
  imports: [
    ChangesComponent,
    ErrorComponent,
    ItemComponent,
    ItemsComponent,
    NodeChangeComponent,
    NodePageHeaderComponent,
    PageComponent,
    RouterLink,
    SituationOnComponent,
    UserLinkLoginComponent,
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
  ],
})
export class NodeChangesPageComponent implements OnInit {
  readonly service = inject(NodeChangesPageService);

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
