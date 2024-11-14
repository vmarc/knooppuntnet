import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyService } from '@app/analysis/strategy';
import { OldPageComponent } from '@app/components/shared/page';
import { RouterService } from '../../../shared/services/router.service';
import { AnalysisSidebarComponent } from '../../analysis/analysis-sidebar.component';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { NetworkFactsComponent } from './components/network-facts.component';
import { NetworkFactsPageService } from './network-facts-page.service';

@Component({
  selector: 'kpn-network-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-old-page>
      <kpn-network-page-header
        pageName="facts"
        pageTitle="Facts"
        i18n-pageTitle="@@network-facts.title"
      />
      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <p i18n="@@network-page.network-not-found">Network not found</p>
          } @else {
            <kpn-network-facts [apiResponse]="response" />
          }
        </div>
      }
      <kpn-analysis-sidebar sidebar />
    </kpn-old-page>
  `,
  providers: [NetworkFactsPageService, AnalysisStrategyService, RouterService],
  standalone: true,
  imports: [
    AnalysisSidebarComponent,
    NetworkPageHeaderComponent,
    OldPageComponent,
    NetworkFactsComponent,
  ],
})
export class NetworkFactsPageComponent implements OnInit {
  protected readonly service = inject(NetworkFactsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
