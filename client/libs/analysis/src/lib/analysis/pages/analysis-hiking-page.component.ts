import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { IconButtonComponent } from '@app/components/shared/icon';
import { IconButtonsComponent } from '@app/components/shared/icon';
import { PageHeaderComponent } from '@app/components/shared/page';
import { AnalysisStrategyService } from '../../strategy';

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

    <kpn-page-header>
      <span class="header-network-type-icon">
        <mat-icon svgIcon="hiking"></mat-icon>
      </span>
      <span i18n="@@network-type.hiking">Hiking</span>
    </kpn-page-header>

    <kpn-icon-buttons>
      <kpn-icon-button
        [routerLink]="nlLink | async"
        icon="netherlands"
        i18n-title="@@country.nl"
        title="The Netherlands"
      />
      <kpn-icon-button
        [routerLink]="beLink | async"
        icon="belgium"
        i18n-title="@@country.be"
        title="Belgium"
      />
      <kpn-icon-button
        [routerLink]="deLink | async"
        icon="germany"
        i18n-title="@@country.de"
        title="Germany"
      />
      <kpn-icon-button
        [routerLink]="frLink | async"
        icon="france"
        i18n-title="@@country.fr"
        title="France"
      />
      <kpn-icon-button
        [routerLink]="esLink | async"
        icon="spain"
        i18n-title="@@country.es"
        title="Spain"
      />
    </kpn-icon-buttons>
  `,
  standalone: true,
  imports: [
    RouterLink,
    PageHeaderComponent,
    MatIconModule,
    IconButtonsComponent,
    IconButtonComponent,
    AsyncPipe,
  ],
})
export class AnalysisHikingPageComponent {
  readonly nlLink = this.analysisStrategyService.link('hiking', 'nl');
  readonly beLink = this.analysisStrategyService.link('hiking', 'be');
  readonly deLink = this.analysisStrategyService.link('hiking', 'de');
  readonly frLink = this.analysisStrategyService.link('hiking', 'fr');
  readonly esLink = this.analysisStrategyService.link('hiking', 'es');

  constructor(private analysisStrategyService: AnalysisStrategyService) {}
}
