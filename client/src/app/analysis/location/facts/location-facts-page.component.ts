import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { actionLocationFactsPageDestroy } from '../store/location.actions';
import { actionLocationFactsPageInit } from '../store/location.actions';
import { selectLocationFactsPage } from '../store/location.selectors';

@Component({
  selector: 'kpn-location-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-location-page-header
      pageName="facts"
      pageTitle="Facts"
      i18n-pageTitle="@@location-facts.title"
    />

    <kpn-error />

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <kpn-location-response [response]="response">
        <kpn-location-facts [locationFacts]="response.result.locationFacts" />
      </kpn-location-response>
    </div>
  `,
})
export class LocationFactsPageComponent implements OnInit, OnDestroy {
  readonly response$ = this.store.select(selectLocationFactsPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionLocationFactsPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionLocationFactsPageDestroy());
  }
}
