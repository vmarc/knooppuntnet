import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionLocationChangesPageDestroy } from '../store/location.actions';
import { actionLocationChangesPageInit } from '../store/location.actions';
import { selectLocationChangesPage } from '../store/location.selectors';

@Component({
  selector: 'kpn-location-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-location-page-header
      pageName="changes"
      pageTitle="Changes"
      i18n-pageTitle="@@location-changes.title"
    />

    <kpn-error/>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <kpn-location-response [response]="response">
        <kpn-location-changes/>
      </kpn-location-response>
    </div>
  `,
})
export class LocationChangesPageComponent implements OnInit, OnDestroy {
  readonly response$ = this.store.select(selectLocationChangesPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionLocationChangesPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionLocationChangesPageDestroy());
  }
}
