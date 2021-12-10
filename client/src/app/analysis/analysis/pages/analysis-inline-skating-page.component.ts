import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisModeService } from '../../mode/analysis-mode.service';

@Component({
  selector: 'kpn-analysis-inline-skating-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li i18n="@@network-type.inline-skating">Inline skating</li>
    </ul>

    <kpn-page-header>
      <span class="header-network-type-icon">
        <mat-icon svgIcon="inline-skating"></mat-icon>
      </span>
      <span i18n="@@network-type.inline-skating">Inline skating</span>
    </kpn-page-header>

    <kpn-icon-buttons>
      <kpn-icon-button
        [routerLink]="nlLink | async"
        icon="netherlands"
        i18n-title="@@country.nl"
        title="The Netherlands"
      ></kpn-icon-button>
    </kpn-icon-buttons>
  `,
})
export class AnalysisInlineSkatingPageComponent {
  readonly nlLink = this.analysisModeService.link('inline-skating', 'nl');

  constructor(private analysisModeService: AnalysisModeService) {}
}
