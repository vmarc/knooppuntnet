import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationDetailsComponent } from './components/location-details.component';
import { LocationDetailsPageService } from './location-details-page.service';

@Component({
  selector: 'ui-location-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-location-page-header
        pageName="details"
        pageTitle="Details"
        i18n-pageTitle="@@location-details.title"
      />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <p i18n="@@location-page.location-not-found">Location not found</p>
          } @else {
            <ui-location-details [response]="response" />
          }
        </div>
      }
    </ui-page>
  `,
  providers: [LocationDetailsPageService, AnalysisStrategyService, RouterService],
  imports: [LocationDetailsComponent, LocationPageHeaderComponent, PageComponent],
})
export class LocationDetailsPageComponent implements OnInit {
  protected readonly service = inject(LocationDetailsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
