import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangesComponent } from '@app/analysis/components/changes/changes.component';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { ItemComponent } from '@app/shared/components/items/item.component';
import { ItemsComponent } from '@app/shared/components/items/items.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { RouterService } from '@app/shared/services/router.service';
import { UserLinkLoginComponent } from '@app/shared/user/user-link-login.component';
import { NodePageHeaderComponent } from '../components/node-page-header.component';
import { NodeChangeComponent } from './components/node-change.component';
import { NodeChangesPageService } from './node-changes-page.service';

@Component({
  selector: 'ui-node-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
      <ui-node-page-header pageName="changes" />

      <ui-error />

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
                  <ui-user-link-login />
                </p>
              </div>
            } @else {
              @if (response.result; as page) {
                <div>
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
                    [totalCount]="page.totalCount"
                    [changeCount]="page.changes.length"
                  >
                    <ui-items>
                      @for (nodeChangeInfo of page.changes; track nodeChangeInfo) {
                        <ui-item [index]="nodeChangeInfo.rowIndex">
                          <ui-node-change [nodeChangeInfo]="nodeChangeInfo" />
                        </ui-item>
                      }
                    </ui-items>
                  </ui-changes>
                </div>
              }
            }
            <ng-template #changes>
              @if (response.result; as page) {
                <div>
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
                    [totalCount]="page.totalCount"
                    [changeCount]="page.changes.length"
                  >
                    <ui-items>
                      @for (nodeChangeInfo of page.changes; track nodeChangeInfo) {
                        <ui-item [index]="nodeChangeInfo.rowIndex">
                          <ui-node-change [nodeChangeInfo]="nodeChangeInfo" />
                        </ui-item>
                      }
                    </ui-items>
                  </ui-changes>
                </div>
              }
            </ng-template>
          }
        </div>
      }
    </ui-page>
  `,
  providers: [NodeChangesPageService, RouterService],
  imports: [
    BreadcrumbComponent,
    ChangesComponent,
    ErrorComponent,
    ItemComponent,
    ItemsComponent,
    NodeChangeComponent,
    NodePageHeaderComponent,
    PageComponent,
    SituationOnComponent,
    UserLinkLoginComponent,
  ],
})
export class NodeChangesPageComponent implements OnInit {
  protected readonly service = inject(NodeChangesPageService);

  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    Breadcrumbs.analysis,
    { label: Breadcrumbs.nodeChangesLabel },
  ];

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
