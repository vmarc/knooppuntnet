import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionNetworkNodesPageInit } from '../store/network.actions';
import { selectNetworkNodesPage } from '../store/network.selectors';

@Component({
  selector: 'kpn-network-nodes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-network-page-header
      pageName="nodes"
      pageTitle="Nodes"
      i18n-pageTitle="@@network-nodes.title"
    ></kpn-network-page-header>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">
        <p i18n="@@network-page.network-not-found">Network not found</p>
      </div>
      <div *ngIf="response.result">
        <p>
          <kpn-situation-on
            [timestamp]="response.situationOn"
          ></kpn-situation-on>
        </p>
        <div
          *ngIf="response.result.nodes.length === 0"
          i18n="@@network-nodes.no-nodes"
        >
          No network nodes in network
        </div>
        <kpn-network-node-table
          *ngIf="response.result.nodes.length > 0"
          [networkType]="response.result.summary.networkType"
          [networkScope]="response.result.summary.networkScope"
          [timeInfo]="response.result.timeInfo"
          [surveyDateInfo]="response.result.surveyDateInfo"
          [nodes]="response.result.nodes"
        ></kpn-network-node-table>
      </div>
    </div>
  `,
})
export class NetworkNodesPageComponent implements OnInit {
  readonly response$ = this.store.select(selectNetworkNodesPage);

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionNetworkNodesPageInit());
  }
}
