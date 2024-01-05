import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
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
  selector: 'kpn-analysis-canoe-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li i18n="@@network-type.canoe">Canoe</li>
      </ul>

      <kpn-page-header>
        <span class="header-network-type-icon">
          <mat-icon svgIcon="canoe" />
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
        />
        <kpn-icon-button
          [routerLink]="frLink$ | async"
          icon="france"
          i18n-title="@@country.fr"
          title="France"
        />
        <!-- eslint-enable @angular-eslint/template/i18n -->
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
export class AnalysisCanoePageComponent {
  private readonly analysisStrategyService = inject(AnalysisStrategyService);
  protected readonly nlLink$ = this.analysisStrategyService.link('canoe', 'nl');
  protected readonly frLink$ = this.analysisStrategyService.link('canoe', 'fr');
}
