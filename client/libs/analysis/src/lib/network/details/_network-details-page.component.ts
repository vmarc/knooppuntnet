import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from '@app/components/shared/page';
import { Store } from '@ngrx/store';
import { AnalysisSidebarComponent } from '../../analysis/analysis-sidebar.component';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { actionNetworkDetailsPageDestroy } from '../store/network.actions';
import { actionNetworkDetailsPageInit } from '../store/network.actions';
import { selectNetworkDetailsPage } from '../store/network.selectors';
import { NetworkDetailsComponent } from './network-details.component';

@Component({
  selector: 'kpn-network-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-network-page-header
        pageName="details"
        pageTitle="Details"
        i18n-pageTitle="@@network-details.title"
      />

      <div *ngIf="apiResponse() as response" class="kpn-spacer-above">
        <div *ngIf="!response.result">
          <p i18n="@@network-page.network-not-found">Network not found</p>
        </div>
        <div *ngIf="response.result">
          <kpn-network-details [response]="response" />
        </div>
      </div>
      <kpn-analysis-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AnalysisSidebarComponent,
    AsyncPipe,
    NetworkDetailsComponent,
    NetworkPageHeaderComponent,
    NgIf,
    PageComponent,
  ],
})
export class NetworkDetailsPageComponent implements OnInit, OnDestroy {
  readonly apiResponse = this.store.selectSignal(selectNetworkDetailsPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionNetworkDetailsPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionNetworkDetailsPageDestroy());
  }
}
