import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { PageComponent } from '../../../../shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { NetworkFactsComponent } from './components/network-facts.component';
import { NetworkFactsPageService } from './network-facts-page.service';

@Component({
  selector: 'kpn-network-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
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
    </kpn-page>
  `,
  providers: [NetworkFactsPageService, AnalysisStrategyService, RouterService],
  imports: [NetworkFactsComponent, NetworkPageHeaderComponent, PageComponent],
})
export class NetworkFactsPageComponent implements OnInit {
  protected readonly service = inject(NetworkFactsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
