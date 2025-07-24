import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { LocationDetailsComponent } from './components/location-details.component';
import { LocationDetailsPageService } from './location-details-page.service';

@Component({
  selector: 'ui-location-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response(); as response) {
      <div class="kpn-spacer-above">
        @if (!response.result) {
          <p i18n="@@location-page.location-not-found">Location not found</p>
        } @else {
          <ui-location-details [response]="response" />
        }
      </div>
    }
  `,
  providers: [LocationDetailsPageService, AnalysisStrategyService],
  imports: [LocationDetailsComponent],
})
export class LocationDetailsPageComponent implements OnInit {
  protected readonly service = inject(LocationDetailsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
