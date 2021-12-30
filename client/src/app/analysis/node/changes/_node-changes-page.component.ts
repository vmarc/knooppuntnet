import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { UserService } from '../../../services/user.service';
import { actionNodeChangesPageIndex } from '../store/node.actions';
import { actionNodeChangesPageInit } from '../store/node.actions';
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
    >
    </kpn-node-page-header>

    <kpn-error></kpn-error>

    <div
      *ngIf="!isLoggedIn()"
      i18n="@@node.login-required"
      class="kpn-spacer-above"
    >
      The details of the node changes history is available to registered
      OpenStreetMap contributors only, after
      <kpn-link-login></kpn-link-login>
      .
    </div>

    <div
      *ngIf="isLoggedIn() && response$ | async as response"
      class="kpn-spacer-above"
    >
      <div *ngIf="!response.result" i18n="@@node.node-not-found">
        Node not found
      </div>
      <div *ngIf="response.result as page">
        <p>
          <kpn-situation-on
            [timestamp]="response.situationOn"
          ></kpn-situation-on>
        </p>
        <kpn-changes
          [totalCount]="page.totalCount"
          [changeCount]="page.changes.length"
          [pageIndex]="pageIndex$ | async"
          (pageIndexChanged)="pageIndexChanged($event)"
        >
          <kpn-items>
            <kpn-item
              *ngFor="let nodeChangeInfo of page.changes"
              [index]="nodeChangeInfo.rowIndex"
            >
              <kpn-node-change
                [nodeChangeInfo]="nodeChangeInfo"
              ></kpn-node-change>
            </kpn-item>
          </kpn-items>
        </kpn-changes>
      </div>
    </div>
  `,
})
export class NodeChangesPageComponent implements OnInit {
  readonly nodeId$ = this.store.select(selectNodeId);
  readonly nodeName$ = this.store.select(selectNodeName);
  readonly changeCount$ = this.store.select(selectNodeChangeCount);
  readonly totalCount$ = this.store.select(selectNodeChangeCount);
  readonly pageIndex$ = this.store.select(selectNodeChangesPageIndex);

  readonly response$ = this.store
    .select(selectNodeChangesPage)
    .pipe(filter((x) => x !== null));

  constructor(
    private userService: UserService,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
    if (this.isLoggedIn()) {
      this.store.dispatch(actionNodeChangesPageInit());
    }
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  pageIndexChanged(pageIndex: number): void {
    this.store.dispatch(actionNodeChangesPageIndex({ pageIndex }));
  }
}
