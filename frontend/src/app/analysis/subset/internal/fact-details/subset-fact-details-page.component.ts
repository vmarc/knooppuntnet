import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatCardTitle } from '@angular/material/card';
import { MatCardHeader } from '@angular/material/card';
import { MatCardContent } from '@angular/material/card';
import { MatCard } from '@angular/material/card';
import { FactLevelComponent } from '@app/analysis/fact/components/fact-level.component';
import { FactInfo } from '@app/analysis/fact/components/fact-info';
import { FactDescriptionComponent } from '@app/analysis/fact/components/fact-description.component';
import { FactNameComponent } from '@app/analysis/fact/components/fact-name.component';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetFactDetailsSummaryComponent } from './components/subset-fact-details-summary.component';
import { SubsetFactDetailsComponent } from './components/subset-fact-details.component';
import { SubsetFactDetailsPageService } from './subset-fact-details-page.service';

@Component({
  selector: 'ui-subset-fact-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-subset-page-header-block
        pageName="facts"
        pageTitle="Facts"
        i18n-pageTitle="@@subset-facts.title"
      />
      <mat-card class="kpn-spacer-above">
        <mat-card-header>
          <mat-card-title>
            <div class="kpn-line">
              <ui-fact-name [fact]="service.factDefinition().fact" />
              <ui-fact-level [factLevel]="service.factDefinition().level" />
            </div>
          </mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <ui-fact-description [factInfo]="factInfo()" />
          @if (service.page(); as page) {
            <ui-subset-fact-details-summary [page]="page" />
          }
        </mat-card-content>
      </mat-card>
      <ui-error />
      @if (service.response(); as response) {
        <div>
          @if (response.result) {
            <div>
              <ui-subset-fact-details [page]="response.result" />
            </div>
          }
        </div>
      }
    </ui-page>
  `,
  styleUrl: './subset-fact-details-page.component.scss',
  providers: [SubsetFactDetailsPageService, AnalysisStrategyService, RouterService],
  imports: [
    ErrorComponent,
    FactDescriptionComponent,
    FactLevelComponent,
    FactNameComponent,
    MatCard,
    MatCardContent,
    MatCardHeader,
    MatCardTitle,
    PageComponent,
    SubsetFactDetailsComponent,
    SubsetFactDetailsSummaryComponent,
    SubsetPageHeaderBlockComponent,
  ],
})
export class SubsetFactDetailsPageComponent implements OnInit {
  protected readonly service = inject(SubsetFactDetailsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }

  factInfo(): FactInfo {
    return new FactInfo(this.service.subsetFact().fact);
  }
}
