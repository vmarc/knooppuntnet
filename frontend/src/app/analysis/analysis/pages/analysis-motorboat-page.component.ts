import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { IconButtonComponent } from '@app/components/shared/icon';
import { IconButtonsComponent } from '@app/components/shared/icon';
import { PageHeaderComponent } from '@app/components/shared/page';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { PageComponent } from '../../../shared/components/shared/page/page.component';
import { RouterService } from '../../../shared/services/router.service';
import { AnalysisStrategyComponent } from '../../strategy';
import { AnalysisStrategyService } from '../../strategy';

@Component({
  selector: 'kpn-analysis-motorboat-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li i18n="@@route-type.motorboat">Motorboat</li>
      </ul>

      <kpn-page-header>
        <span class="header-route-type-icon">
          <nz-icon nzType="motorboat" />
        </span>
        <span i18n="@@route-type.motorboat">Motorboat</span>
      </kpn-page-header>

      <kpn-analysis-strategy />

      <kpn-icon-buttons>
        <kpn-icon-button
          [routerLink]="nlLink()"
          icon="netherlands"
          i18n-title="@@country.nl"
          title="The Netherlands"
        />
      </kpn-icon-buttons>
    </kpn-page>
  `,
  providers: [AnalysisStrategyService, RouterService],
  imports: [
    AnalysisStrategyComponent,
    IconButtonComponent,
    IconButtonsComponent,
    MatIconModule,
    NzIconDirective,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class AnalysisMotorboatPageComponent implements OnInit {
  private readonly analysisStrategyService = inject(AnalysisStrategyService);
  protected readonly nlLink = this.analysisStrategyService.link('motorboat', 'nl');

  ngOnInit(): void {
    this.analysisStrategyService.init();
  }
}
