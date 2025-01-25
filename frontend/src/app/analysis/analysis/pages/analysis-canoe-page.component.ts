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
  selector: 'kpn-analysis-canoe-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li i18n="@@route-type.canoe">Canoe</li>
      </ul>

      <kpn-page-header>
        <span class="header-route-type-icon">
          <nz-icon nzType="canoe" />
        </span>
        <span i18n="@@route-type.canoe">Canoe</span>
      </kpn-page-header>

      <kpn-analysis-strategy />

      <kpn-icon-buttons>
        <!-- icon attribute does not need translation -->
        <!-- eslint-disable @angular-eslint/template/i18n -->
        <kpn-icon-button
          [routerLink]="nlLink()"
          icon="netherlands"
          i18n-title="@@country.nl"
          title="The Netherlands"
        />
        <kpn-icon-button
          [routerLink]="frLink()"
          icon="france"
          i18n-title="@@country.fr"
          title="France"
        />
        <!-- eslint-enable @angular-eslint/template/i18n -->
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
export class AnalysisCanoePageComponent implements OnInit {
  private readonly analysisStrategyService = inject(AnalysisStrategyService);
  protected readonly nlLink = this.analysisStrategyService.link('canoe', 'nl');
  protected readonly frLink = this.analysisStrategyService.link('canoe', 'fr');

  ngOnInit(): void {
    this.analysisStrategyService.init();
  }
}
