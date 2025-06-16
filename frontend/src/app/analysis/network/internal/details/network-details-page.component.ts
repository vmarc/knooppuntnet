import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NetworkNotFoundComponent } from '@app/analysis/network/internal/components/network-not-found.component';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { NetworkMapSidebarComponent } from '../map/components/network-map-sidebar.component';
import { NetworkDetailsComponent } from './components/network-details.component';
import { NetworkDetailsPageService } from './network-details-page.service';

@Component({
  selector: 'ui-network-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-network-page-header
        pageName="details"
        pageTitle="Details"
        i18n-pageTitle="@@network-details.title"
      />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <ui-network-not-found />
          } @else {
            <ui-network-details [response]="response" />
          }
        </div>
      }
      <ui-network-map-sidebar />
    </ui-page>
  `,
  providers: [NetworkDetailsPageService, AnalysisStrategyService, RouterService],
  imports: [
    NetworkDetailsComponent,
    NetworkMapSidebarComponent,
    NetworkNotFoundComponent,
    NetworkPageHeaderComponent,
    PageComponent,
  ],
})
export class NetworkDetailsPageComponent implements OnInit {
  protected readonly service = inject(NetworkDetailsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
