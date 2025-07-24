import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { IntegerFormatPipe } from '@app/shared/components/format/integer-format.pipe';
import { PageWidthService } from '@app/shared/components/page-width.service';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { MarkdownComponent } from 'ngx-markdown';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetNetworkListComponent } from './components/subset-network-list.component';
import { SubsetNetworkTableComponent } from './components/subset-network-table.component';
import { SubsetNetworksPageService } from './subset-networks-page.service';

@Component({
  selector: 'ui-subset-networks-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-subset-page-header-block
      pageName="networks"
      pageTitle="Networks"
      i18n-pageTitle="@@subset-networks.title"
    />

    <ui-error />

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
            @if (large()) {
              <ui-subset-network-table [networks]="response.result.networks" />
            } @else {
              <ui-subset-network-list [networks]="response.result.networks" />
            }
          </div>
        }
      </div>
    }
  `,
  providers: [SubsetNetworksPageService, AnalysisStrategyService],
  imports: [
    ErrorComponent,
    IntegerFormatPipe,
    MarkdownComponent,
    SituationOnComponent,
    SubsetNetworkListComponent,
    SubsetNetworkTableComponent,
    SubsetPageHeaderBlockComponent,
    IntegerFormatPipe,
  ],
})
export class SubsetNetworksPageComponent implements OnInit {
  protected readonly service = inject(SubsetNetworksPageService);
  private readonly pageWidthService = inject(PageWidthService);
  protected readonly large = this.pageWidthService.isVeryLarge;

  ngOnInit(): void {
    this.service.onInit();
  }
}
