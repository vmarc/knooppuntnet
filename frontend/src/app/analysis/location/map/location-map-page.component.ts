import { AsyncPipe } from '@angular/common';
import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { RouterService } from '../../../shared/services/router.service';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationSidebarComponent } from '../location-sidebar.component';
import { LocationMapComponent } from './components/location-map.component';
import { LocationMapPageService } from './location-map-page.service';

@Component({
  selector: 'kpn-location-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page [showFooter]="false">
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
      <kpn-location-sidebar sidebar />
    </kpn-page>
  `,
  providers: [LocationMapPageService, RouterService],
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
export class LocationMapPageComponent implements OnInit {
  protected readonly service = inject(LocationMapPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
