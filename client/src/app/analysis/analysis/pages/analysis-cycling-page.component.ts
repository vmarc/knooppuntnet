import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { AnalysisModeService } from './analysis-mode.service';

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

    <kpn-page-header i18n="@@network-type.cycling">Cycling</kpn-page-header>

    <kpn-analysis-mode></kpn-analysis-mode>

    <kpn-icon-button
      [routerLink]="nlLink | async"
      icon="netherlands"
      i18n="@@country.nl"
      >The Netherlands</kpn-icon-button
    >
    <kpn-icon-button
      [routerLink]="beLink | async"
      icon="belgium"
      i18n="@@country.be"
      >Belgium</kpn-icon-button
    >
    <kpn-icon-button
      [routerLink]="deLink | async"
      icon="germany"
      i18n="@@country.de"
      >Germany</kpn-icon-button
    >
    <kpn-icon-button
      [routerLink]="frLink | async"
      icon="france"
      i18n="@@country.fr"
      >France</kpn-icon-button
    >
    <kpn-icon-button
      [routerLink]="atLink | async"
      icon="austria"
      i18n="@@country.at"
      >Austria</kpn-icon-button
    >
    <kpn-icon-button
      [routerLink]="esLink | async"
      icon="spain"
      i18n="@@country.es"
      >Spain</kpn-icon-button
    >
  `,
})
export class AnalysisCyclingPageComponent {
  readonly nlLink: Observable<string>;
  readonly beLink: Observable<string>;
  readonly deLink: Observable<string>;
  readonly frLink: Observable<string>;
  readonly atLink: Observable<string>;
  readonly esLink: Observable<string>;

  constructor(private analysisModeService: AnalysisModeService) {
    this.nlLink = analysisModeService.link('cycling', 'nl');
    this.beLink = analysisModeService.link('cycling', 'be');
    this.deLink = analysisModeService.link('cycling', 'de');
    this.frLink = analysisModeService.link('cycling', 'fr');
    this.atLink = analysisModeService.link('cycling', 'at');
    this.esLink = analysisModeService.link('cycling', 'es');
  }
}
