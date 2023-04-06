import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { selectUserLoggedIn } from '@app/core/user/user.selectors';
import { Store } from '@ngrx/store';
import { selectDefined } from '@app/core/core.state';
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

@Component({
  selector: 'kpn-node-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a [routerLink]="'/analysis'" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li i18n="@@breadcrumb.node-changes">Node changes</li>
    </ul>

    <kpn-node-page-header
      pageName="changes"
      [nodeId]="nodeId$ | async"
      [nodeName]="nodeName$ | async"
      [changeCount]="changeCount$ | async"
    />

    <kpn-error />

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <p *ngIf="!response.result; else nodeFound" i18n="@@node.node-not-found">
        Node not found
      </p>

      <ng-template #nodeFound>
        <div
          *ngIf="(loggedIn$ | async) === false; else loggedIn"
          i18n="@@node.login-required"
          class="kpn-spacer-above"
        >
          The details of the node changes history is available to registered
          OpenStreetMap contributors only, after
          <kpn-link-login></kpn-link-login>
          .
        </div>

        <ng-template #loggedIn>
          <div *ngIf="response.result as page">
            <p>
              <kpn-situation-on
                [timestamp]="response.situationOn"
              ></kpn-situation-on>
            </p>
            <kpn-changes
              [impact]="impact$ | async"
              [pageSize]="pageSize$ | async"
              [pageIndex]="pageIndex$ | async"
              (impactChange)="onImpactChange($event)"
              (pageSizeChange)="onPageSizeChange($event)"
              (pageIndexChange)="onPageIndexChange($event)"
              [totalCount]="page.totalCount"
              [changeCount]="page.changes.length"
            >
              <kpn-items>
                <kpn-item
                  *ngFor="let nodeChangeInfo of page.changes"
                  [index]="nodeChangeInfo.rowIndex"
                >
                  <kpn-node-change [nodeChangeInfo]="nodeChangeInfo" />
                </kpn-item>
              </kpn-items>
            </kpn-changes>
          </div>
        </ng-template>
      </ng-template>
    </div>
  `,
})
export class NodeChangesPageComponent implements OnInit {
  readonly nodeId$ = this.store.select(selectNodeId);
  readonly nodeName$ = this.store.select(selectNodeName);
  readonly changeCount$ = this.store.select(selectNodeChangeCount);
  readonly impact$ = this.store.select(selectNodeChangesPageImpact);
  readonly pageSize$ = this.store.select(selectNodeChangesPageSize);
  readonly pageIndex$ = this.store.select(selectNodeChangesPageIndex);
  readonly loggedIn$ = this.store.select(selectUserLoggedIn);
  readonly response$ = selectDefined(this.store, selectNodeChangesPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionNodeChangesPageInit());
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
