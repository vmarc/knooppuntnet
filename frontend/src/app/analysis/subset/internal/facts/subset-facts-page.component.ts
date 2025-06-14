import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FactCount } from '@api/common/fact-count';
import { SubsetFactsPage } from '@api/common/subset/subset-facts-page';
import { ApiResponse } from '@api/custom/api-response';
import { Fact } from '@api/common/fact';
import { FactInfo } from '@app/analysis/fact/components/fact-info';
import { FactLevel } from '@api/common/fact-level';
import { Facts } from '@app/analysis/fact/components/facts';
import { FactDescriptionComponent } from '@app/analysis/fact/components/fact-description.component';
import { FactLevelComponent } from '@app/analysis/fact/components/fact-level.component';
import { FactNameComponent } from '@app/analysis/fact/components/fact-name.component';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { ItemComponent } from '@app/shared/components/items/item.component';
import { ItemsComponent } from '@app/shared/components/items/items.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { RouterService } from '@app/shared/services/router.service';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetFactsPageService } from './subset-facts-page.service';

@Component({
  selector: 'ui-subset-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-subset-page-header-block
        pageName="facts"
        pageTitle="Facts"
        i18n-pageTitle="@@subset-facts.title"
      />

      <ui-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          <p>
            <ui-situation-on [timestamp]="response.situationOn" />
          </p>
          @if (!hasFacts(response)) {
            <p class="kpn-line">
              <span i18n="@@subset-facts.no-facts">No facts</span>
              <ui-icon-happy />
            </p>
          } @else {
            <div class="kpn-line">
              <ui-items>
                @for (factCount of response.result.factCounts; track factCount) {
                  <ui-item [index]="$index">
                    <div class="kpn-line">
                      <a [routerLink]="factCount.fact">
                        <ui-fact-name [fact]="factCount.fact" />
                      </a>
                      <span>({{ factCount.count }})</span>
                      <ui-fact-level [factLevel]="factLevel(factCount.fact)" />
                    </div>
                    <ui-fact-description [factInfo]="factInfo(factCount)" />
                  </ui-item>
                }
              </ui-items>
            </div>
          }
        </div>
      }
    </ui-page>
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
