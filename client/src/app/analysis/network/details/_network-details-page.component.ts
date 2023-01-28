import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { actionNetworkDetailsPageInit } from '../store/network.actions';
import { selectNetworkDetailsPage } from '../store/network.selectors';

@Component({
  selector: 'kpn-network-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-network-page-header
      pageName="details"
      pageTitle="Details"
      i18n-pageTitle="@@network-details.title"
    />

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">
        <p i18n="@@network-page.network-not-found">Network not found</p>
      </div>
      <div *ngIf="response.result">
        <kpn-network-details [response]="response" />
      </div>
    </div>
  `,
})
export class NetworkDetailsPageComponent implements OnInit {
  readonly response$ = this.store.select(selectNetworkDetailsPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionNetworkDetailsPageInit());
  }
}
