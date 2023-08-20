import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { IconButtonComponent } from '@app/components/shared/icon';
import { IconButtonsComponent } from '@app/components/shared/icon';
import { PageComponent } from '@app/components/shared/page';
import { PageHeaderComponent } from '@app/components/shared/page';
import { AnalysisStrategyService } from '../../strategy';
import { AnalysisSidebarComponent } from '../analysis-sidebar.component';

@Component({
  selector: 'kpn-analysis-cycling-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li i18n="@@network-type.cycling">Cycling</li>
      </ul>

      <kpn-page-header>
        <span class="header-network-type-icon">
          <mat-icon svgIcon="cycling" />
        </span>
        <span i18n="@@network-type.cycling">Cycling</span>
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
          [routerLink]="atLink | async"
          icon="austria"
          i18n-title="@@country.at"
          title="Austria"
        />
        <kpn-icon-button
          [routerLink]="esLink | async"
          icon="spain"
          i18n-title="@@country.es"
          title="Spain"
        />
        <kpn-icon-button
          [routerLink]="dkLink | async"
          icon="denmark"
          i18n-title="@@country.dk"
          title="Denmark"
        />
      </kpn-icon-buttons>
      <kpn-analysis-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AnalysisSidebarComponent,
    AsyncPipe,
    IconButtonComponent,
    IconButtonsComponent,
    MatIconModule,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class AnalysisCyclingPageComponent {
  readonly nlLink = this.analysisStrategyService.link('cycling', 'nl');
  readonly beLink = this.analysisStrategyService.link('cycling', 'be');
  readonly deLink = this.analysisStrategyService.link('cycling', 'de');
  readonly frLink = this.analysisStrategyService.link('cycling', 'fr');
  readonly atLink = this.analysisStrategyService.link('cycling', 'at');
  readonly esLink = this.analysisStrategyService.link('cycling', 'es');
  readonly dkLink = this.analysisStrategyService.link('cycling', 'dk');

  constructor(private analysisStrategyService: AnalysisStrategyService) {}
}
