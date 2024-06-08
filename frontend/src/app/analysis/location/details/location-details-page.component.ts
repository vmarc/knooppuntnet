import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyService } from '@app/analysis/strategy';
import { PageComponent } from '@app/components/shared/page';
import { RouterService } from '../../../shared/services/router.service';
import { AnalysisSidebarComponent } from '../../analysis/analysis-sidebar.component';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationDetailsComponent } from './components/location-details.component';
import { LocationDetailsPageService } from './location-details-page.service';

@Component({
  selector: 'kpn-location-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-location-page-header
        pageName="details"
        pageTitle="Details"
        i18n-pageTitle="@@location-details.title"
      />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <p i18n="@@location-page.location-not-found">Location not found</p>
          } @else {
            <kpn-location-details [response]="response" />
          }
        </div>
      }
      <kpn-analysis-sidebar sidebar />
    </kpn-page>
  `,
  providers: [LocationDetailsPageService, AnalysisStrategyService, RouterService],
  standalone: true,
  imports: [
    AnalysisSidebarComponent,
    LocationDetailsComponent,
    LocationPageHeaderComponent,
    PageComponent,
  ],
})
export class LocationDetailsPageComponent implements OnInit {
  protected readonly service = inject(LocationDetailsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
