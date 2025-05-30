import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Fact } from '@api/common/fact';
import { FactDescriptionComponent } from '@app/analysis/fact/components/fact-description.component';
import { FactNameComponent } from '@app/analysis/fact/components/fact-name.component';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { ItemComponent } from '@app/shared/components/items/item.component';
import { ItemsComponent } from '@app/shared/components/items/items.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { FactInfo } from '@app/analysis/fact/components/fact-info';
import { Facts } from '@app/analysis/fact/components/facts';

@Component({
  selector: 'ui-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <h1 i18n="@@fact-page.title">All facts</h1>

      <ui-items>
        @for (factName of allFactNames(); track $index) {
          <ui-item [index]="$index">
            <p>
              <ui-fact-name [fact]="factName" />
            </p>
            <ui-fact-description [factInfo]="factInfo(factName)" />
          </ui-item>
        }
      </ui-items>
    </ui-page>
  `,
  providers: [AnalysisStrategyService, RouterService],
  imports: [
    FactDescriptionComponent,
    FactNameComponent,
    ItemComponent,
    ItemsComponent,
    PageComponent,
  ],
})
export class FactsPageComponent {
  allFactNames() {
    return Facts.allFactNames;
  }

  factInfo(fact: Fact): FactInfo {
    return new FactInfo(fact);
  }
}
