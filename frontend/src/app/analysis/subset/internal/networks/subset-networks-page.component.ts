import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { IntegerFormatPipe } from '@app/shared/components/format/integer-format.pipe';
import { PageWidthService } from '@app/shared/components/page-width.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { MarkdownModule } from 'ngx-markdown';
import { RouterService } from '@app/shared/services/router.service';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetNetworkListComponent } from './components/subset-network-list.component';
import { SubsetNetworkTableComponent } from './components/subset-network-table.component';
import { SubsetNetworksPageService } from './subset-networks-page.service';

@Component({
  selector: 'kpn-subset-networks-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-subset-page-header-block
        pageName="networks"
        pageTitle="Networks"
        i18n-pageTitle="@@subset-networks.title"
      />

      <kpn-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (response.result.networks.length === 0) {
            <div i18n="@@subset-networks.no-networks">No networks</div>
          } @else {
            <div>
              <p>
                <kpn-situation-on [timestamp]="response.situationOn" />
              </p>
              <markdown i18n="@@subset-networks.summary">
                _There are __{{ response.result.networkCount | integer }}__ networks, with a total
                of __{{ response.result.nodeCount | integer }}__ nodes and __{{
                  response.result.routeCount | integer
                }}__ routes with an overall length of __{{ response.result.km | integer }}__ km._
              </markdown>
              @if (large()) {
                <kpn-subset-network-table [networks]="response.result.networks" />
              } @else {
                <kpn-subset-network-list [networks]="response.result.networks" />
              }
            </div>
          }
        </div>
      }
    </kpn-page>
  `,
  providers: [SubsetNetworksPageService, AnalysisStrategyService, RouterService],
  imports: [
    ErrorComponent,
    IntegerFormatPipe,
    MarkdownModule,
    PageComponent,
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
