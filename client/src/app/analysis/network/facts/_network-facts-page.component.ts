import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionNetworkFactsPageInit } from '../store/network.actions';
import { selectNetworkFactsPage } from '../store/network.selectors';

@Component({
  selector: 'kpn-network-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-network-page-header
      pageName="facts"
      pageTitle="Facts"
      i18n-pageTitle="@@network-facts.title"
    />

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">
        <p i18n="@@network-page.network-not-found">Network not found</p>
      </div>
      <div *ngIf="response.result">
        <kpn-situation-on [timestamp]="response.situationOn"/>

        <p *ngIf="response.result.facts.length === 0" class="kpn-line">
          <span i18n="@@network-facts.no-facts">No facts</span>
          <kpn-icon-happy/>
        </p>

        <kpn-items *ngIf="response.result.facts.length > 0">
          <kpn-item
            *ngFor="let fact of response.result.facts; let i = index"
            [index]="i"
          >
            <kpn-network-fact
              [fact]="fact"
              [networkType]="response.result.summary.networkType"
            />
          </kpn-item>
        </kpn-items>
      </div>
    </div>
  `,
})
export class NetworkFactsPageComponent implements OnInit {
  readonly response$ = this.store.select(selectNetworkFactsPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionNetworkFactsPageInit());
  }
}
