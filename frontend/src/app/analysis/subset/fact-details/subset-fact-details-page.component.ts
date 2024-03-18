import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatCardTitle } from '@angular/material/card';
import { MatCardHeader } from '@angular/material/card';
import { MatCardContent } from '@angular/material/card';
import { MatCard } from '@angular/material/card';
import { FactLevelComponent } from '@app/analysis/fact';
import { Facts } from '@app/analysis/fact';
import { FactInfo } from '@app/analysis/fact';
import { FactDescriptionComponent } from '@app/analysis/fact';
import { FactNameComponent } from '@app/analysis/fact';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { RouterService } from '../../../shared/services/router.service';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetSidebarComponent } from '../subset-sidebar.component';
import { SubsetFactDetailsSummaryComponent } from './components/subset-fact-details-summary.component';
import { SubsetFactDetailsComponent } from './components/subset-fact-details.component';
import { SubsetFactDetailsStore } from './subset-fact-details.store';

@Component({
  selector: 'kpn-subset-fact-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-subset-page-header-block
        pageName="facts"
        pageTitle="Facts"
        i18n-pageTitle="@@subset-facts.title"
      />
      <mat-card class="kpn-spacer-above">
        <mat-card-header>
          <mat-card-title>
            <div class="kpn-line">
              <kpn-fact-name [fact]="factDefinition().name" />
              <kpn-fact-level [factLevel]="factDefinition().level"></kpn-fact-level>
            </div>
          </mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <kpn-fact-description [factInfo]="factInfo()" />
          @if (page()) {
            <kpn-subset-fact-details-summary [page]="page()" />
          }
        </mat-card-content>
      </mat-card>
      <kpn-error />
      @if (store.response(); as response) {
        <div>
          @if (response.result) {
            <div>
              <kpn-subset-fact-details [page]="response.result" />
            </div>
          }
        </div>
      }
      <kpn-subset-sidebar sidebar />
    </kpn-page>
  `,
  styleUrl: './subset-fact-details-page.component.scss',
  providers: [/*SubsetFactDetailsStore,*/ RouterService],
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
    FactLevelComponent,
  ],
})
export class SubsetFactDetailsPageComponent {
  protected readonly store = inject(SubsetFactDetailsStore);
  protected readonly subsetFact = this.store.subsetFact;
  protected readonly page = computed(() => this.store.response()?.result);
  protected readonly factDefinition = computed(() => Facts.facts.get(this.subsetFact().factName));

  factInfo(): FactInfo {
    return new FactInfo(this.subsetFact().factName);
  }
}
