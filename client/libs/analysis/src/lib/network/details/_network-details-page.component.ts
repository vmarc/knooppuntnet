import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { actionNetworkDetailsPageInit } from '../store/network.actions';
import { selectNetworkDetailsPage } from '../store/network.selectors';
import { NetworkDetailsComponent } from './network-details.component';

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
  standalone: true,
  imports: [
    NetworkPageHeaderComponent,
    NgIf,
    NetworkDetailsComponent,
    AsyncPipe,
  ],
})
export class NetworkDetailsPageComponent implements OnInit {
  readonly response$ = this.store.select(selectNetworkDetailsPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionNetworkDetailsPageInit());
  }
}
