import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ChangesComponent } from '@app/analysis/components/changes';
import { ErrorComponent } from '@app/components/shared/error';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { PageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { Store } from '@ngrx/store';
import { UserLinkLoginComponent } from '../../../shared/user';
import { UserStore } from '../../../shared/user/user.store';
import { NodePageHeaderComponent } from '../components/node-page-header.component';
import { actionNodeChangesPageDestroy } from '../store/node.actions';
import { actionNodeChangesPageSize } from '../store/node.actions';
import { actionNodeChangesPageImpact } from '../store/node.actions';
import { actionNodeChangesPageIndex } from '../store/node.actions';
import { actionNodeChangesPageInit } from '../store/node.actions';
import { selectNodeChangesPageSize } from '../store/node.selectors';
import { selectNodeChangesPageImpact } from '../store/node.selectors';
import { selectNodeChangesPageIndex } from '../store/node.selectors';
import { selectNodeChangesPage } from '../store/node.selectors';
import { selectNodeChangeCount } from '../store/node.selectors';
import { selectNodeName } from '../store/node.selectors';
import { selectNodeId } from '../store/node.selectors';
import { NodeChangeComponent } from './node-change.component';
import { NodeChangesSidebarComponent } from './node-changes-sidebar.component';

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

      <kpn-node-page-header
        pageName="changes"
        [nodeId]="nodeId()"
        [nodeName]="nodeName()"
        [changeCount]="changeCount()"
      />

      <kpn-error />

      @if (apiResponse(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <p i18n="@@node.node-not-found">Node not found</p>
          } @else {
            @if (loggedIn() === false) {
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
                    [impact]="impact()"
                    [pageSize]="pageSize()"
                    [pageIndex]="pageIndex()"
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
                    [impact]="impact()"
                    [pageSize]="pageSize()"
                    [pageIndex]="pageIndex()"
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
  standalone: true,
  imports: [
    AsyncPipe,
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
export class NodeChangesPageComponent implements OnInit, OnDestroy {
  private readonly userStore = inject(UserStore);
  readonly loggedIn = this.userStore.loggedIn;

  private readonly store = inject(Store);
  readonly nodeId = this.store.selectSignal(selectNodeId);
  readonly nodeName = this.store.selectSignal(selectNodeName);
  readonly changeCount = this.store.selectSignal(selectNodeChangeCount);
  readonly impact = this.store.selectSignal(selectNodeChangesPageImpact);
  readonly pageSize = this.store.selectSignal(selectNodeChangesPageSize);
  readonly pageIndex = this.store.selectSignal(selectNodeChangesPageIndex);
  readonly apiResponse = this.store.selectSignal(selectNodeChangesPage);

  ngOnInit(): void {
    this.store.dispatch(actionNodeChangesPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionNodeChangesPageDestroy());
  }

  onImpactChange(impact: boolean): void {
    this.store.dispatch(actionNodeChangesPageImpact({ impact }));
  }

  onPageSizeChange(pageSize: number): void {
    this.store.dispatch(actionNodeChangesPageSize({ pageSize }));
  }

  onPageIndexChange(pageIndex: number): void {
    this.store.dispatch(actionNodeChangesPageIndex({ pageIndex }));
  }
}
