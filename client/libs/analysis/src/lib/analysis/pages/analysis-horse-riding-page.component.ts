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
  selector: 'kpn-analysis-horse-riding-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
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
export class AnalysisHorseRidingPageComponent {
  readonly nlLink = this.analysisStrategyService.link('horse-riding', 'nl');
  readonly beLink = this.analysisStrategyService.link('horse-riding', 'be');

  constructor(private analysisStrategyService: AnalysisStrategyService) {}
}