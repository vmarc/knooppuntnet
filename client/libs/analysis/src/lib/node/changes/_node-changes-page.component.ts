import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ChangesComponent } from '@app/analysis/components/changes';
import { ErrorComponent } from '@app/components/shared/error';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { LinkLoginComponent } from '@app/components/shared/link';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { selectUserLoggedIn } from '@app/core';
import { Store } from '@ngrx/store';
import { NodePageHeaderComponent } from '../components/node-page-header.component';
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
      [nodeId]="nodeId()"
      [nodeName]="nodeName()"
      [changeCount]="changeCount()"
    />

    <kpn-error />

    <div *ngIf="apiResponse() as response" class="kpn-spacer-above">
      <p *ngIf="!response.result; else nodeFound" i18n="@@node.node-not-found">
        Node not found
      </p>

      <ng-template #nodeFound>
        <div
          *ngIf="loggedIn() === false; else changes"
          i18n="@@node.login-required"
          class="kpn-spacer-above"
        >
          The details of the node changes history is available to registered
          OpenStreetMap contributors only, after
          <kpn-link-login></kpn-link-login>
          .
        </div>

        <ng-template #changes>
          <div *ngIf="response.result as page">
            <p>
              <kpn-situation-on
                [timestamp]="response.situationOn"
              ></kpn-situation-on>
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
  standalone: true,
  imports: [
    RouterLink,
    NodePageHeaderComponent,
    ErrorComponent,
    NgIf,
    LinkLoginComponent,
    SituationOnComponent,
    ChangesComponent,
    ItemsComponent,
    NgFor,
    ItemComponent,
    NodeChangeComponent,
    AsyncPipe,
  ],
})
export class NodeChangesPageComponent implements OnInit {
  readonly nodeId = this.store.selectSignal(selectNodeId);
  readonly nodeName = this.store.selectSignal(selectNodeName);
  readonly changeCount = this.store.selectSignal(selectNodeChangeCount);
  readonly impact = this.store.selectSignal(selectNodeChangesPageImpact);
  readonly pageSize = this.store.selectSignal(selectNodeChangesPageSize);
  readonly pageIndex = this.store.selectSignal(selectNodeChangesPageIndex);
  readonly loggedIn = this.store.selectSignal(selectUserLoggedIn);
  readonly apiResponse = this.store.selectSignal(selectNodeChangesPage);

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
