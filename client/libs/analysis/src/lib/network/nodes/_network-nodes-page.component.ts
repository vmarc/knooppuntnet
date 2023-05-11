import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { Store } from '@ngrx/store';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { actionNetworkNodesPageInit } from '../store/network.actions';
import { selectNetworkNodesPage } from '../store/network.selectors';
import { NetworkNodeTableComponent } from './network-node-table.component';

@Component({
  selector: 'kpn-network-nodes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-network-page-header
      pageName="nodes"
      pageTitle="Nodes"
      i18n-pageTitle="@@network-nodes.title"
    />

    <div *ngIf="apiResponse() as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">
        <p i18n="@@network-page.network-not-found">Network not found</p>
      </div>
      <div *ngIf="response.result as page">
        <p>
          <kpn-situation-on [timestamp]="response.situationOn" />
        </p>
        <div *ngIf="page.nodes.length === 0" i18n="@@network-nodes.no-nodes">
          No network nodes in network
        </div>
        <kpn-network-node-table
          *ngIf="page.nodes.length > 0"
          [networkType]="page.summary.networkType"
          [networkScope]="page.summary.networkScope"
          [timeInfo]="page.timeInfo"
          [surveyDateInfo]="page.surveyDateInfo"
          [nodes]="page.nodes"
        />
      </div>
    </div>
  `,
  standalone: true,
  imports: [
    NetworkPageHeaderComponent,
    NgIf,
    SituationOnComponent,
    NetworkNodeTableComponent,
    AsyncPipe,
  ],
})
export class NetworkNodesPageComponent implements OnInit {
  readonly apiResponse = this.store.selectSignal(selectNetworkNodesPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionNetworkNodesPageInit());
  }
}
