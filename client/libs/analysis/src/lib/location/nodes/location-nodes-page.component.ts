import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { Store } from '@ngrx/store';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { actionLocationNodesPageDestroy } from '../store/location.actions';
import { actionLocationNodesPageInit } from '../store/location.actions';
import { selectLocationNodesPage } from '../store/location.selectors';
import { LocationNodesComponent } from './location-nodes.component';

@Component({
  selector: 'kpn-location-nodes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-location-page-header
      pageName="nodes"
      pageTitle="Nodes"
      i18n-pageTitle="@@location-nodes.title"
    />

    <kpn-error />
    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <kpn-location-response [response]="response">
        <kpn-location-nodes [page]="response.result" />
      </kpn-location-response>
    </div>
  `,
  standalone: true,
  imports: [
    LocationPageHeaderComponent,
    ErrorComponent,
    NgIf,
    LocationResponseComponent,
    LocationNodesComponent,
    AsyncPipe,
  ],
})
export class LocationNodesPageComponent implements OnInit, OnDestroy {
  readonly response$ = this.store.select(selectLocationNodesPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionLocationNodesPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionLocationNodesPageDestroy());
  }
}
