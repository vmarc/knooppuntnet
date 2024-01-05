import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { Store } from '@ngrx/store';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationSidebarComponent } from '../location-sidebar.component';
import { actionLocationFactsPageDestroy } from '../store/location.actions';
import { actionLocationFactsPageInit } from '../store/location.actions';
import { selectLocationFactsPage } from '../store/location.selectors';
import { LocationFactsComponent } from './location-facts.component';

@Component({
  selector: 'kpn-location-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-location-page-header
        pageName="facts"
        pageTitle="Facts"
        i18n-pageTitle="@@location-facts.title"
      />

      <kpn-error />

      @if (apiResponse(); as response) {
        <div class="kpn-spacer-above">
          <kpn-location-response [response]="response">
            <kpn-location-facts [locationFacts]="response.result.locationFacts" />
          </kpn-location-response>
        </div>
      }
      <kpn-location-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    ErrorComponent,
    LocationFactsComponent,
    LocationPageHeaderComponent,
    LocationResponseComponent,
    LocationSidebarComponent,
    PageComponent,
  ],
})
export class LocationFactsPageComponent implements OnInit, OnDestroy {
  private readonly store = inject(Store);
  readonly apiResponse = this.store.selectSignal(selectLocationFactsPage);

  ngOnInit(): void {
    this.store.dispatch(actionLocationFactsPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionLocationFactsPageDestroy());
  }
}
