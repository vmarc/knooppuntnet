import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { RouterService } from '../../../shared/services/router.service';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationSidebarComponent } from '../location-sidebar.component';
import { LocationFactsComponent } from './components/location-facts.component';
import { LocationFactsStore } from './location-facts.store';

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

      @if (store.response(); as response) {
        <div class="kpn-spacer-above">
          <kpn-location-response [response]="response">
            <kpn-location-facts [locationFacts]="response.result.locationFacts" />
          </kpn-location-response>
        </div>
      }
      <kpn-location-sidebar sidebar />
    </kpn-page>
  `,
  providers: [LocationFactsStore, RouterService],
  standalone: true,
  imports: [
    ErrorComponent,
    LocationFactsComponent,
    LocationPageHeaderComponent,
    LocationResponseComponent,
    LocationSidebarComponent,
    PageComponent,
  ],
})
export class LocationFactsPageComponent {
  protected readonly store = inject(LocationFactsStore);
}
