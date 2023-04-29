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

    <kpn-page-header>
      <span class="header-network-type-icon">
        <mat-icon svgIcon="motorboat"></mat-icon>
      </span>
      <span i18n="@@network-type.motorboat">Motorboat</span>
    </kpn-page-header>

    <kpn-icon-buttons>
      <kpn-icon-button
        [routerLink]="nlLink | async"
        icon="netherlands"
        i18n-title="@@country.nl"
        title="The Netherlands"
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
export class AnalysisMotorboatPageComponent {
  readonly nlLink = this.analysisStrategyService.link('motorboat', 'nl');

  constructor(private analysisStrategyService: AnalysisStrategyService) {}
}