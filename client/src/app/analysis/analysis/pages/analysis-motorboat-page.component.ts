import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { AnalysisModeService } from './analysis-mode.service';

@Component({
  selector: 'kpn-analysis-motorboat-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li i18n="@@network-type.motorboat">Motorboat</li>
    </ul>

    <kpn-page-header i18n="@@network-type.motorboat">Motorboat</kpn-page-header>

    <kpn-analysis-mode></kpn-analysis-mode>

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
export class AnalysisMotorboatPageComponent {
  readonly nlLink: Observable<string>;

  constructor(private analysisModeService: AnalysisModeService) {
    this.nlLink = analysisModeService.link('motorboat', 'nl');
  }
}
