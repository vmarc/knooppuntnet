import { OnInit } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { IntegerFormatPipe } from '@app/shared/components/format/integer-format.pipe';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { MarkdownComponent } from 'ngx-markdown';
import { SubsetNetworkListComponent } from './components/subset-network-list.component';
import { SubsetNetworksPageService } from './subset-networks-page.service';

@Component({
  selector: 'ui-subset-networks-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response(); as response) {
      <div class="kpn-spacer-above">
        @if (response.result.networks.length === 0) {
          <div i18n="@@subset-networks.no-networks">No networks</div>
        } @else {
          <div>
            <p>
              <ui-situation-on [timestamp]="response.situationOn" />
            </p>
            <markdown i18n="@@subset-networks.summary">
              _There are __{{ response.result.networkCount | integer }}__ networks, with a total of
              __{{ response.result.nodeCount | integer }}__ nodes and __{{
                response.result.routeCount | integer
              }}__ routes with an overall length of __{{ response.result.km | integer }}__ km._
            </markdown>
            <ui-subset-network-list [networks]="response.result.networks" />
          </div>
        }
      </div>
    }
  `,
  providers: [SubsetNetworksPageService, AnalysisStrategyService],
  imports: [
    IntegerFormatPipe,
    MarkdownComponent,
    SituationOnComponent,
    SubsetNetworkListComponent,
    IntegerFormatPipe,
  ],
})
export class SubsetNetworksPageComponent implements OnInit, OnDestroy {
  protected readonly service = inject(SubsetNetworksPageService);

  ngOnInit(): void {
    this.service.onInit();
  }

  ngOnDestroy(): void {
    this.service.onDestroy();
  }
}
