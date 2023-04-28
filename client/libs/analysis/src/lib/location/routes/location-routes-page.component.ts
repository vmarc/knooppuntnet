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
import { actionLocationRoutesPageDestroy } from '../store/location.actions';
import { actionLocationRoutesPageInit } from '../store/location.actions';
import { selectLocationRoutesPage } from '../store/location.selectors';
import { LocationRoutesComponent } from './location-routes.component';

@Component({
  selector: 'kpn-location-routes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-location-page-header
      pageName="routes"
      pageTitle="Routes"
      i18n-pageTitle="@@location-routes.title"
    />

    <kpn-error />

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <kpn-location-response [response]="response">
        <kpn-location-routes [page]="response.result" />
      </kpn-location-response>
    </div>
  `,
  standalone: true,
  imports: [
    LocationPageHeaderComponent,
    ErrorComponent,
    NgIf,
    LocationResponseComponent,
    LocationRoutesComponent,
    AsyncPipe,
  ],
})
export class LocationRoutesPageComponent implements OnInit, OnDestroy {
  readonly response$ = this.store.select(selectLocationRoutesPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionLocationRoutesPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionLocationRoutesPageDestroy());
  }
}
