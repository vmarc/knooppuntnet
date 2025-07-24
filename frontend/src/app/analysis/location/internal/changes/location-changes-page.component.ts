import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationChangesComponent } from './components/location-changes.component';
import { LocationChangesPageService } from './location-changes-page.service';

@Component({
  selector: 'ui-location-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response(); as response) {
      <div class="kpn-spacer-above">
        <ui-location-response [response]="response">
          <ui-location-changes [page]="response.result" />
        </ui-location-response>
      </div>
    }
  `,
  providers: [LocationChangesPageService],
  imports: [LocationChangesComponent, LocationResponseComponent],
})
export class LocationChangesPageComponent implements OnInit {
  protected readonly service = inject(LocationChangesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
