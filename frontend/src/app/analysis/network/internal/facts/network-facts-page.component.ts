import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NetworkNotFoundComponent } from '@app/analysis/network/internal/components/network-not-found.component';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { NetworkFactsComponent } from './components/network-facts.component';
import { NetworkFactsPageService } from './network-facts-page.service';

@Component({
  selector: 'ui-network-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-network-page-header
        pageName="facts"
        pageTitle="Facts"
        i18n-pageTitle="@@network-facts.title"
      />
      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <ui-network-not-found />
          } @else {
            <ui-network-facts [apiResponse]="response" />
          }
        </div>
      }
    </ui-page>
  `,
  providers: [NetworkFactsPageService, AnalysisStrategyService, RouterService],
  imports: [
    NetworkFactsComponent,
    NetworkNotFoundComponent,
    NetworkPageHeaderComponent,
    PageComponent,
  ],
})
export class NetworkFactsPageComponent implements OnInit {
  protected readonly service = inject(NetworkFactsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
