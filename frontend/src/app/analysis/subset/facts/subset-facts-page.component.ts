import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FactCount } from '@api/common';
import { SubsetFactsPage } from '@api/common/subset';
import { ApiResponse } from '@api/custom';
import { Fact } from '@api/common';
import { FactInfo } from '@app/analysis/fact';
import { FactLevel } from '@api/common';
import { Facts } from '@app/analysis/fact';
import { FactDescriptionComponent } from '@app/analysis/fact';
import { FactLevelComponent } from '@app/analysis/fact';
import { FactNameComponent } from '@app/analysis/fact';
import { AnalysisStrategyService } from '@app/analysis/strategy';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { ItemComponent } from '@app/shared/components/items/item.component';
import { ItemsComponent } from '@app/shared/components/items/items.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { RouterService } from '../../../shared/services/router.service';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetFactsPageService } from './subset-facts-page.service';

@Component({
  selector: 'kpn-subset-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-subset-page-header-block
        pageName="facts"
        pageTitle="Facts"
        i18n-pageTitle="@@subset-facts.title"
      />

      <kpn-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          <p>
            <kpn-situation-on [timestamp]="response.situationOn" />
          </p>
          @if (!hasFacts(response)) {
            <p class="kpn-line">
              <span i18n="@@subset-facts.no-facts">No facts</span>
              <kpn-icon-happy />
            </p>
          } @else {
            <div class="kpn-line">
              <kpn-items>
                @for (factCount of response.result.factCounts; track factCount; let i = $index) {
                  <kpn-item [index]="i">
                    <div class="kpn-line">
                      <a [routerLink]="factCount.fact">
                        <kpn-fact-name [fact]="factCount.fact" />
                      </a>
                      <span>({{ factCount.count }})</span>
                      <kpn-fact-level [factLevel]="factLevel(factCount.fact)" />
                    </div>
                    <kpn-fact-description [factInfo]="factInfo(factCount)" />
                  </kpn-item>
                }
              </kpn-items>
            </div>
          }
        </div>
      }
    </kpn-page>
  `,
  providers: [SubsetFactsPageService, AnalysisStrategyService, RouterService],
  imports: [
    ErrorComponent,
    FactDescriptionComponent,
    FactLevelComponent,
    FactNameComponent,
    IconHappyComponent,
    ItemComponent,
    ItemsComponent,
    PageComponent,
    RouterLink,
    SituationOnComponent,
    SubsetPageHeaderBlockComponent,
  ],
})
export class SubsetFactsPageComponent implements OnInit {
  protected readonly service = inject(SubsetFactsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }

  hasFacts(response: ApiResponse<SubsetFactsPage>): boolean {
    return response.result && response.result.subsetInfo.factCount > 0;
  }

  factLevel(fact: Fact): FactLevel {
    return Facts.factLevel(fact);
  }

  factInfo(factCount: FactCount): FactInfo {
    return new FactInfo(factCount.fact);
  }
}
