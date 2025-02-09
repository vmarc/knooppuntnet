import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationMapComponent } from './components/location-map.component';
import { LocationMapPageService } from './location-map-page.service';

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

      @if (service.response(); as response) {
        <kpn-location-response [response]="response">
          <kpn-location-map />
        </kpn-location-response>
      }
    </kpn-page>
  `,
  providers: [LocationMapPageService, RouterService],
  imports: [
    ErrorComponent,
    LocationMapComponent,
    LocationPageHeaderComponent,
    LocationResponseComponent,
    PageComponent,
  ],
})
export class LocationMapPageComponent implements OnInit {
  protected readonly service = inject(LocationMapPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
