import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionLocationNodesPageDestroy } from '../store/location.actions';
import { actionLocationNodesPageInit } from '../store/location.actions';
import { selectLocationNodesPage } from '../store/location.selectors';

@Component({
  selector: 'kpn-location-nodes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-location-page-header
      pageName="nodes"
      pageTitle="Nodes"
      i18n-pageTitle="@@location-nodes.title"
    />

    <kpn-error/>
    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <kpn-location-response [response]="response">
        <kpn-location-nodes [page]="response.result"/>
      </kpn-location-response>
    </div>
  `,
})
export class LocationNodesPageComponent implements OnInit, OnDestroy {
  readonly response$ = this.store.select(selectLocationNodesPage);

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionLocationNodesPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionLocationNodesPageDestroy());
  }
}
