import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { MatCardTitle } from '@angular/material/card';
import { MatCardHeader } from '@angular/material/card';
import { MatCardContent } from '@angular/material/card';
import { MatCard } from '@angular/material/card';
import { FactInfo } from '@app/analysis/fact';
import { FactDescriptionComponent } from '@app/analysis/fact';
import { FactNameComponent } from '@app/analysis/fact';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { SubsetFact } from '@app/kpn/common';
import { Store } from '@ngrx/store';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { actionSubsetFactDetailsPageInit } from '../store/subset.actions';
import { selectSubsetFact } from '../store/subset.selectors';
import { selectSubsetFactDetailsPage } from '../store/subset.selectors';
import { SubsetSidebarComponent } from '../subset-sidebar.component';
import { SubsetFactDetailsSummaryComponent } from './subset-fact-details-summary.component';
import { SubsetFactDetailsComponent } from './subset-fact-details.component';

@Component({
  selector: 'kpn-subset-fact-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      @if (subsetFact(); as fact) {
        <kpn-subset-page-header-block
          pageName="facts"
          pageTitle="Facts"
          i18n-pageTitle="@@subset-facts.title"
        />
        <mat-card class="kpn-spacer-above">
          <mat-card-header>
            <mat-card-title>
              <kpn-fact-name [fact]="fact.factName" />
            </mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <kpn-fact-description [factInfo]="factInfo(fact)" />
            @if (page()) {
              <kpn-subset-fact-details-summary [page]="page()" />
            }
          </mat-card-content>
        </mat-card>
        <kpn-error />
        @if (apiResponse(); as response) {
          <div>
            @if (response.result) {
              <div>
                <kpn-subset-fact-details [page]="response.result" />
              </div>
            }
          </div>
        }
      }
      <kpn-subset-sidebar sidebar />
    </kpn-page>
  `,
  styleUrl: './_subset-fact-details-page.component.scss',
  standalone: true,
  imports: [
    ErrorComponent,
    FactDescriptionComponent,
    FactNameComponent,
    MatCard,
    MatCardContent,
    MatCardHeader,
    MatCardTitle,
    PageComponent,
    SubsetFactDetailsComponent,
    SubsetFactDetailsSummaryComponent,
    SubsetPageHeaderBlockComponent,
    SubsetSidebarComponent,
  ],
})
export class SubsetFactDetailsPageComponent implements OnInit {
  private readonly store = inject(Store);
  protected readonly subsetFact = this.store.selectSignal(selectSubsetFact);
  protected readonly apiResponse = this.store.selectSignal(selectSubsetFactDetailsPage);
  protected readonly page = computed(() => this.apiResponse()?.result);

  ngOnInit(): void {
    this.store.dispatch(actionSubsetFactDetailsPageInit());
  }

  factInfo(subsetFact: SubsetFact): FactInfo {
    return new FactInfo(subsetFact.factName);
  }
}
