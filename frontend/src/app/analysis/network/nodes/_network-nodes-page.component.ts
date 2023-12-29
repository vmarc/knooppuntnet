import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { PageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { Store } from '@ngrx/store';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { actionNetworkNodesPageDestroy } from '../store/network.actions';
import { actionNetworkNodesPageInit } from '../store/network.actions';
import { selectNetworkNodesPage } from '../store/network.selectors';
import { NetworkNodeTableComponent } from './network-node-table.component';
import { NetworkNodesSidebarComponent } from './network-nodes-sidebar.component';

@Component({
  selector: 'kpn-network-nodes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-network-page-header
        pageName="nodes"
        pageTitle="Nodes"
        i18n-pageTitle="@@network-nodes.title"
      />

      @if (apiResponse(); as response) {
        @if (!response.result) {
          <p i18n="@@network-page.network-not-found">Network not found</p>
        } @else {
          @if (response.result; as page) {
            <p>
              <kpn-situation-on [timestamp]="response.situationOn" />
            </p>
            @if (page.nodes.length === 0) {
              <div i18n="@@network-nodes.no-nodes">No network nodes in network</div>
            } @else {
              <kpn-network-node-table
                [networkType]="page.summary.networkType"
                [networkScope]="page.summary.networkScope"
                [timeInfo]="page.timeInfo"
                [surveyDateInfo]="page.surveyDateInfo"
                [nodes]="page.nodes"
              />
            }
          }
        }
      }
      <kpn-network-nodes-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    NetworkNodeTableComponent,
    NetworkNodesSidebarComponent,
    NetworkPageHeaderComponent,
    PageComponent,
    SituationOnComponent,
  ],
})
export class NetworkNodesPageComponent implements OnInit, OnDestroy {
  readonly apiResponse = this.store.selectSignal(selectNetworkNodesPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionNetworkNodesPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionNetworkNodesPageDestroy());
  }
}
