import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { IconHappyComponent } from '@app/components/shared/icon';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { Store } from '@ngrx/store';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { actionNetworkFactsPageInit } from '../store/network.actions';
import { selectNetworkFactsPage } from '../store/network.selectors';
import { NetworkFactComponent } from './network-fact.component';

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
        <kpn-situation-on [timestamp]="response.situationOn" />

        <p *ngIf="response.result.facts.length === 0" class="kpn-line">
          <span i18n="@@network-facts.no-facts">No facts</span>
          <kpn-icon-happy />
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
  standalone: true,
  imports: [
    NetworkPageHeaderComponent,
    NgIf,
    SituationOnComponent,
    IconHappyComponent,
    ItemsComponent,
    NgFor,
    ItemComponent,
    NetworkFactComponent,
    AsyncPipe,
  ],
})
export class NetworkFactsPageComponent implements OnInit {
  readonly response$ = this.store.select(selectNetworkFactsPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionNetworkFactsPageInit());
  }
}
