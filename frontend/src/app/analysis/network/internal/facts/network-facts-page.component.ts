import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { NetworkFactsComponent } from './components/network-facts.component';
import { NetworkFactsPageService } from './network-facts-page.service';

@Component({
  selector: 'ui-network-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response(); as response) {
      <div class="kpn-spacer-above">
        <ui-network-facts [apiResponse]="response" />
      </div>
    }
  `,
  providers: [NetworkFactsPageService, AnalysisStrategyService],
  imports: [NetworkFactsComponent],
})
export class NetworkFactsPageComponent implements OnInit {
  protected readonly service = inject(NetworkFactsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
