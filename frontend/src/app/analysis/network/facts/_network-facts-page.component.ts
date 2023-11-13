import { AsyncPipe } from '@angular/common';
import { NgFor } from '@angular/common';
import { NgIf } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { IconHappyComponent } from '@app/components/shared/icon';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { PageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { Store } from '@ngrx/store';
import { AnalysisSidebarComponent } from '../../analysis/analysis-sidebar.component';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { actionNetworkFactsPageDestroy } from '../store/network.actions';
import { actionNetworkFactsPageInit } from '../store/network.actions';
import { selectNetworkFactsPage } from '../store/network.selectors';
import { NetworkFactComponent } from './network-fact.component';

@Component({
  selector: 'kpn-network-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-network-page-header
        pageName="facts"
        pageTitle="Facts"
        i18n-pageTitle="@@network-facts.title"
      />

      <div *ngIf="apiResponse() as response" class="kpn-spacer-above">
        <div *ngIf="!response.result">
          <p i18n="@@network-page.network-not-found">Network not found</p>
        </div>
        <div *ngIf="response.result as page">
          <kpn-situation-on [timestamp]="response.situationOn" />

          <p *ngIf="page.facts.length === 0" class="kpn-line">
            <span i18n="@@network-facts.no-facts">No facts</span>
            <kpn-icon-happy />
          </p>

          <kpn-items *ngIf="page.facts.length > 0">
            <kpn-item
              *ngFor="let fact of page.facts; let i = index"
              [index]="i"
            >
              <kpn-network-fact
                [fact]="fact"
                [networkType]="page.summary.networkType"
              />
            </kpn-item>
          </kpn-items>
        </div>
      </div>
      <kpn-analysis-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AnalysisSidebarComponent,
    AsyncPipe,
    IconHappyComponent,
    ItemComponent,
    ItemsComponent,
    NetworkFactComponent,
    NetworkPageHeaderComponent,
    NgFor,
    NgIf,
    PageComponent,
    SituationOnComponent,
  ],
})
export class NetworkFactsPageComponent implements OnInit, OnDestroy {
  readonly apiResponse = this.store.selectSignal(selectNetworkFactsPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionNetworkFactsPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionNetworkFactsPageDestroy());
  }
}
