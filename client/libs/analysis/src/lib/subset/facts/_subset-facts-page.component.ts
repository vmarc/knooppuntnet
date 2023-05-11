import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FactCount } from '@api/common';
import { SubsetFactsPage } from '@api/common/subset';
import { ApiResponse } from '@api/custom';
import { Fact } from '@api/custom';
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
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { Store } from '@ngrx/store';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { actionSubsetFactRefsLoad } from '../store/subset.actions';
import { actionSubsetFactsPageInit } from '../store/subset.actions';
import { selectSubsetFactsPage } from '../store/subset.selectors';

@Component({
  selector: 'kpn-subset-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-subset-page-header-block
      pageName="facts"
      pageTitle="Facts"
      i18n-pageTitle="@@subset-facts.title"
    />

    <kpn-error />

    <div *ngIf="apiResponse() as response" class="kpn-spacer-above">
      <p>
        <kpn-situation-on [timestamp]="response.situationOn" />
      </p>
      <p *ngIf="!hasFacts(response)" class="kpn-line">
        <span i18n="@@subset-facts.no-facts">No facts</span>
        <kpn-icon-happy />
      </p>
      <div *ngIf="hasFacts(response)" class="kpn-line">
        <kpn-items>
          <kpn-item
            *ngFor="let factCount of response.result.factCounts; let i = index"
            [index]="i"
          >
            <div class="kpn-line">
              <a [routerLink]="factCount.fact">
                <kpn-fact-name [fact]="factCount.fact" />
              </a>
              <span>({{ factCount.count }})</span>
              <kpn-fact-level [factLevel]="factLevel(factCount.fact)" />
              <a
                rel="nofollow"
                (click)="edit(factCount.fact)"
                title="Open in editor (like JOSM)"
                i18n-title="@@edit.link.title"
                i18n="@@edit.link"
                >edit</a
              >
            </div>
            <kpn-fact-description [factInfo]="factInfo(factCount)" />
          </kpn-item>
        </kpn-items>
      </div>
    </div>
  `,
  standalone: true,
  imports: [
    SubsetPageHeaderBlockComponent,
    ErrorComponent,
    NgIf,
    SituationOnComponent,
    IconHappyComponent,
    ItemsComponent,
    NgFor,
    ItemComponent,
    RouterLink,
    FactNameComponent,
    FactLevelComponent,
    FactDescriptionComponent,
    AsyncPipe,
  ],
})
export class SubsetFactsPageComponent implements OnInit {
  readonly apiResponse = this.store.selectSignal(selectSubsetFactsPage);

  constructor(private store: Store) {}

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
