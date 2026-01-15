import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { NetworkMapSidebarComponent } from '../map/components/network-map-sidebar.component';
import { NetworkDetailsComponent } from './components/network-details.component';
import { NetworkDetailsPageService } from './network-details-page.service';

@Component({
  selector: 'ui-network-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response(); as response) {
      <div class="kpn-spacer-above">
        <ui-network-details [networkId]="networkId()" [response]="response" />
      </div>
    }
    <ui-network-map-sidebar />
  `,
  providers: [NetworkDetailsPageService, AnalysisStrategyService],
  imports: [NetworkDetailsComponent, NetworkMapSidebarComponent],
})
export class NetworkDetailsPageComponent implements OnInit {
  protected readonly service = inject(NetworkDetailsPageService);
  readonly networkId = this.service.networkId;

  ngOnInit(): void {
    this.service.onInit();
  }
}
