import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FactCount } from '@api/common';
import { SubsetFactsPage } from '@api/common/subset';
import { ApiResponse } from '@api/custom';
import { Fact } from '@api/custom';
import { EditLinkComponent } from '@app/analysis/components/edit';
import { FactInfo } from '@app/analysis/fact';
import { FactLevel } from '@app/analysis/fact';
import { Facts } from '@app/analysis/fact';
import { FactDescriptionComponent } from '@app/analysis/fact';
import { FactLevelComponent } from '@app/analysis/fact';
import { FactNameComponent } from '@app/analysis/fact';
import { ErrorComponent } from '@app/components/shared/error';
import { IconHappyComponent } from '@app/components/shared/icon';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { PageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { Store } from '@ngrx/store';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { actionSubsetFactRefsLoad } from '../store/subset.actions';
import { actionSubsetFactsPageInit } from '../store/subset.actions';
import { selectSubsetFactsPage } from '../store/subset.selectors';
import { SubsetSidebarComponent } from '../subset-sidebar.component';

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

      @if (apiResponse(); as response) {
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
                      <kpn-edit-link (edit)="edit(factCount.fact)" />
                    </div>
                    <kpn-fact-description [factInfo]="factInfo(factCount)" />
                  </kpn-item>
                }
              </kpn-items>
            </div>
          }
        </div>
      }
      <kpn-subset-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AsyncPipe,
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
    SubsetSidebarComponent,
    EditLinkComponent,
  ],
})
export class SubsetFactsPageComponent implements OnInit {
  private readonly store = inject(Store);
  protected readonly apiResponse = this.store.selectSignal(selectSubsetFactsPage);

  ngOnInit(): void {
    this.store.dispatch(actionSubsetFactsPageInit());
  }

  hasFacts(response: ApiResponse<SubsetFactsPage>): boolean {
    return response.result && response.result.subsetInfo.factCount > 0;
  }

  factLevel(fact: Fact): FactLevel {
    return Facts.factLevels.get(fact);
  }

  edit(fact: Fact): void {
    this.store.dispatch(actionSubsetFactRefsLoad({ fact }));
  }

  factInfo(factCount: FactCount): FactInfo {
    return new FactInfo(factCount.fact);
  }
}
