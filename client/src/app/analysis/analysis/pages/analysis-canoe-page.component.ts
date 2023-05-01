import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategyService } from '../../strategy/analysis-strategy.service';

@Component({
  selector: 'kpn-analysis-canoe-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li i18n="@@network-type.canoe">Canoe</li>
    </ul>

    <kpn-page-header>
      <span class="header-network-type-icon">
        <mat-icon svgIcon="canoe"></mat-icon>
      </span>
      <span i18n="@@network-type.canoe">Canoe</span>
    </kpn-page-header>

    <kpn-icon-buttons>
      <!-- icon attribute does not need translation -->
      <!-- eslint-disable @angular-eslint/template/i18n -->
      <kpn-icon-button
        [routerLink]="nlLink$ | async"
        icon="netherlands"
        i18n-title="@@country.nl"
        title="The Netherlands"
      ></kpn-icon-button>
      <kpn-icon-button
        [routerLink]="frLink$ | async"
        icon="france"
        i18n-title="@@country.fr"
        title="France"
      ></kpn-icon-button>
      <!-- eslint-enable @angular-eslint/template/i18n -->
    </kpn-icon-buttons>
  `,
})
export class AnalysisCanoePageComponent {
  protected readonly nlLink$ = this.analysisStrategyService.link('canoe', 'nl');
  protected readonly frLink$ = this.analysisStrategyService.link('canoe', 'fr');

  constructor(private analysisStrategyService: AnalysisStrategyService) {}
}
