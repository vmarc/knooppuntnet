import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyComponent } from '@app/analysis/strategy';
import { AnalysisStrategyService } from '@app/analysis/strategy';
import { NzDividerComponent } from 'ng-zorro-antd/divider';
import { PageComponent } from '../../../shared/components/shared/page/page.component';
import { RouterService } from '../../../shared/services/router.service';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { NetworkMapSidebarComponent } from '../map/components/network-map-sidebar.component';
import { NetworkDetailsComponent } from './components/network-details.component';
import { NetworkDetailsPageService } from './network-details-page.service';

@Component({
  selector: 'kpn-network-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-analysis-strategy />
      <nz-divider />
      <kpn-network-page-header
        pageName="details"
        pageTitle="Details"
        i18n-pageTitle="@@network-details.title"
      />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <p i18n="@@network-page.network-not-found">Network not found</p>
          } @else {
            <kpn-network-details [response]="response" />
          }
        </div>
      }
      <kpn-network-map-sidebar />
    </kpn-page>
  `,
  providers: [NetworkDetailsPageService, AnalysisStrategyService, RouterService],
  imports: [
    AnalysisStrategyComponent,
    NetworkDetailsComponent,
    NetworkPageHeaderComponent,
    NzDividerComponent,
    PageComponent,
    NetworkMapSidebarComponent,
  ],
})
export class NetworkDetailsPageComponent implements OnInit {
  protected readonly service = inject(NetworkDetailsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
