import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { RouterService } from '../../../shared/services/router.service';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationSidebarComponent } from '../location-sidebar.component';
import { LocationMapComponent } from './location-map.component';
import { LocationMapStore } from './location-map.store';

@Component({
  selector: 'kpn-location-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-location-page-header
        pageName="map"
        pageTitle="Map"
        i18n-pageTitle="@@location-map.title"
      />

      <kpn-error />

      @if (store.response(); as response) {
        <kpn-location-response [response]="response">
          <kpn-location-map />
        </kpn-location-response>
      }
      <kpn-location-sidebar sidebar />
    </kpn-page>
  `,
  providers: [LocationMapStore, RouterService],
  standalone: true,
  imports: [
    AsyncPipe,
    ErrorComponent,
    LocationMapComponent,
    LocationPageHeaderComponent,
    LocationResponseComponent,
    LocationSidebarComponent,
    PageComponent,
  ],
})
export class LocationMapPageComponent {
  protected readonly store = inject(LocationMapStore);
}
