import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationMapComponent } from './components/location-map.component';
import { LocationMapPageService } from './location-map-page.service';

@Component({
  selector: 'ui-location-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response(); as response) {
      <ui-location-response [response]="response">
        <ui-location-map />
      </ui-location-response>
    }
  `,
  providers: [LocationMapPageService],
  imports: [LocationMapComponent, LocationResponseComponent],
})
export class LocationMapPageComponent implements OnInit {
  protected readonly service = inject(LocationMapPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
