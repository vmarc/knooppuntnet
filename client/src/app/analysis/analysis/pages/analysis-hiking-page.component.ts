import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { AnalysisModeService } from './analysis-mode.service';

@Component({
  selector: 'kpn-analysis-hiking-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li i18n="@@network-type.hiking">Hiking</li>
    </ul>

    <kpn-page-header i18n="@@network-type.hiking">Hiking</kpn-page-header>

    <kpn-analysis-mode></kpn-analysis-mode>

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
    </kpn-icon-buttons>
  `,
})
export class AnalysisHikingPageComponent {
  readonly nlLink: Observable<string>;
  readonly beLink: Observable<string>;
  readonly deLink: Observable<string>;
  readonly frLink: Observable<string>;

  constructor(private analysisModeService: AnalysisModeService) {
    this.nlLink = analysisModeService.link('hiking', 'nl');
    this.beLink = analysisModeService.link('hiking', 'be');
    this.deLink = analysisModeService.link('hiking', 'de');
    this.frLink = analysisModeService.link('hiking', 'fr');
  }
}
