import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationRoutesComponent } from './components/location-routes.component';
import { LocationRoutesPageService } from './location-routes-page.service';

@Component({
  selector: 'ui-location-routes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response(); as response) {
      <div class="kpn-spacer-above">
        <ui-location-response [response]="response">
          <ui-location-routes [page]="response.result" />
        </ui-location-response>
      </div>
    }
  `,
  providers: [LocationRoutesPageService],
  imports: [LocationResponseComponent, LocationRoutesComponent],
})
export class LocationRoutesPageComponent implements OnInit {
  protected readonly service = inject(LocationRoutesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
