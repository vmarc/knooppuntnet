import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisModeService } from '../../mode/analysis-mode.service';

@Component({
  selector: 'kpn-analysis-cycling-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li i18n="@@network-type.cycling">Cycling</li>
    </ul>

    <kpn-page-header>
      <span class="header-network-type-icon">
        <mat-icon svgIcon="cycling"></mat-icon>
      </span>
      <span i18n="@@network-type.cycling">Cycling</span>
    </kpn-page-header>

    <kpn-icon-buttons>
      <kpn-icon-button
        [routerLink]="nlLink | async"
        icon="netherlands"
        i18n-title="@@country.nl"
        title="The Netherlands"
      ></kpn-icon-button>
      <kpn-icon-button
        [routerLink]="beLink | async"
        icon="belgium"
        i18n-title="@@country.be"
        title="Belgium"
      ></kpn-icon-button>
      <kpn-icon-button
        [routerLink]="deLink | async"
        icon="germany"
        i18n-title="@@country.de"
        title="Germany"
      ></kpn-icon-button>
      <kpn-icon-button
        [routerLink]="frLink | async"
        icon="france"
        i18n-title="@@country.fr"
        title="France"
      ></kpn-icon-button>
      <kpn-icon-button
        [routerLink]="atLink | async"
        icon="austria"
        i18n-title="@@country.at"
        title="Austria"
      ></kpn-icon-button>
      <kpn-icon-button
        [routerLink]="esLink | async"
        icon="spain"
        i18n-title="@@country.es"
        title="Spain"
      ></kpn-icon-button>
    </kpn-icon-buttons>
  `,
})
export class AnalysisCyclingPageComponent {
  readonly nlLink = this.analysisModeService.link('cycling', 'nl');
  readonly beLink = this.analysisModeService.link('cycling', 'be');
  readonly deLink = this.analysisModeService.link('cycling', 'de');
  readonly frLink = this.analysisModeService.link('cycling', 'fr');
  readonly atLink = this.analysisModeService.link('cycling', 'at');
  readonly esLink = this.analysisModeService.link('cycling', 'es');

  constructor(private analysisModeService: AnalysisModeService) {}
}
