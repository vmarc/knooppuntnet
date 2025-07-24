import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationFactsComponent } from './components/location-facts.component';
import { LocationFactsPageService } from './location-facts-page.service';

@Component({
  selector: 'ui-location-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response(); as response) {
      <div class="kpn-spacer-above">
        <ui-location-response [response]="response">
          <ui-location-facts [locationFacts]="response.result.locationFacts" />
        </ui-location-response>
      </div>
    }
  `,
  providers: [LocationFactsPageService],
  imports: [LocationFactsComponent, LocationResponseComponent],
})
export class LocationFactsPageComponent implements OnInit {
  protected readonly service = inject(LocationFactsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
