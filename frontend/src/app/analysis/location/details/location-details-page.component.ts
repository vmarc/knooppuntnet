import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyService } from '@app/analysis/strategy';
import { SidebarFooterComponent } from '@app/components/shared/sidebar';
import { PageComponent } from '../../../shared/components/shared/page/page.component';
import { RouterService } from '../../../shared/services/router.service';
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
    </kpn-page>
    <kpn-sidebar-footer />
  `,
  providers: [LocationDetailsPageService, AnalysisStrategyService, RouterService],
  imports: [
    LocationDetailsComponent,
    LocationPageHeaderComponent,
    PageComponent,
    SidebarFooterComponent,
  ],
})
export class LocationDetailsPageComponent implements OnInit {
  protected readonly service = inject(LocationDetailsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
