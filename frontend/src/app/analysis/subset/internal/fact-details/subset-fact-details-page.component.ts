import { input } from '@angular/core';
import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Fact } from '@api/common/fact';
import { FactLevelComponent } from '@app/analysis/fact/components/fact-level.component';
import { FactDescriptionComponent } from '@app/analysis/fact/components/fact-description.component';
import { FactNameComponent } from '@app/analysis/fact/components/fact-name.component';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { NzCardComponent } from 'ng-zorro-antd/card';
import { SubsetFactDetailsSummaryComponent } from './components/subset-fact-details-summary.component';
import { SubsetFactDetailsComponent } from './components/subset-fact-details.component';
import { SubsetFactDetailsPageService } from './subset-fact-details-page.service';

@Component({
  selector: 'ui-subset-fact-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-card class="kpn-spacer-above" [nzTitle]="headerTemplate">
      <ui-fact-description [fact]="fact()" />
      @if (service.page(); as page) {
        <ui-subset-fact-details-summary [page]="page" />
      }
    </nz-card>

    <ng-template #headerTemplate>
      <div class="kpn-line">
        <ui-fact-name [fact]="service.factDefinition().fact" />
        <ui-fact-level [factLevel]="service.factDefinition().level" />
      </div>
    </ng-template>

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
  `,
  styleUrl: './subset-fact-details-page.component.scss',
  providers: [SubsetFactDetailsPageService, AnalysisStrategyService],
  imports: [
    ErrorComponent,
    FactDescriptionComponent,
    FactLevelComponent,
    FactNameComponent,
    NzCardComponent,
    SubsetFactDetailsComponent,
    SubsetFactDetailsSummaryComponent,
  ],
})
export class SubsetFactDetailsPageComponent implements OnInit {
  protected readonly service = inject(SubsetFactDetailsPageService);

  readonly fact = input<Fact>(undefined);

  ngOnInit(): void {
    this.service.onInit(this.fact());
  }
}
