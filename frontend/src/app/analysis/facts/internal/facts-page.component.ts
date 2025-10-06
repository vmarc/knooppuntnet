import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FactDescriptionComponent } from '@app/analysis/fact/components/fact-description.component';
import { FactNameComponent } from '@app/analysis/fact/components/fact-name.component';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { ItemComponent } from '@app/shared/components/items/item.component';
import { ItemsComponent } from '@app/shared/components/items/items.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { Facts } from '@app/analysis/fact/components/facts';

@Component({
  selector: 'ui-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <h1 i18n="@@fact-page.title">All facts</h1>

      <ui-items>
        @for (fact of facts(); track fact) {
          <ui-item [index]="$index">
            <p>
              <ui-fact-name [fact]="fact" />
            </p>
            <ui-fact-description [fact]="fact" />
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
  facts() {
    return Facts.facts;
  }
}
