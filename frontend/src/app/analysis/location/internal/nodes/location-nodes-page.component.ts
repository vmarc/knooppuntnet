import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationNodesComponent } from './components/location-nodes.component';
import { LocationNodesPageService } from './location-nodes-page.service';

@Component({
  selector: 'ui-location-nodes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response(); as response) {
      <div class="kpn-spacer-above">
        <ui-location-response [response]="response">
          <ui-location-nodes [page]="response.result" />
        </ui-location-response>
      </div>
    }
  `,
  providers: [LocationNodesPageService],
  imports: [LocationNodesComponent, LocationResponseComponent],
})
export class LocationNodesPageComponent implements OnInit {
  protected readonly service = inject(LocationNodesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
