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
  selector: 'kpn-analysis-horse-riding-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li i18n="@@network-type.horse-riding">Horse riding</li>
      </ul>

      <kpn-page-header>
        <span class="header-network-type-icon">
          <mat-icon svgIcon="horse-riding" />
        </span>
        <span i18n="@@network-type.horse-riding">Horse riding</span>
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
export class AnalysisHorseRidingPageComponent {
  readonly nlLink = this.analysisStrategyService.link('horse-riding', 'nl');
  readonly beLink = this.analysisStrategyService.link('horse-riding', 'be');

  constructor(private analysisStrategyService: AnalysisStrategyService) {}
}
