import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { Store } from '@ngrx/store';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { actionLocationChangesPageDestroy } from '../store/location.actions';
import { actionLocationChangesPageInit } from '../store/location.actions';
import { selectLocationChangesPage } from '../store/location.selectors';
import { LocationChangesComponent } from './location-changes.component';

@Component({
  selector: 'kpn-location-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-location-page-header
      pageName="changes"
      pageTitle="Changes"
      i18n-pageTitle="@@location-changes.title"
    />

    <kpn-error />

    <div *ngIf="apiResponse() as response" class="kpn-spacer-above">
      <kpn-location-response [response]="response">
        <kpn-location-changes />
      </kpn-location-response>
    </div>
  `,
  standalone: true,
  imports: [
    LocationPageHeaderComponent,
    ErrorComponent,
    NgIf,
    LocationResponseComponent,
    LocationChangesComponent,
    AsyncPipe,
  ],
})
export class LocationChangesPageComponent implements OnInit, OnDestroy {
  readonly apiResponse = this.store.selectSignal(selectLocationChangesPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionLocationChangesPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionLocationChangesPageDestroy());
  }
}
