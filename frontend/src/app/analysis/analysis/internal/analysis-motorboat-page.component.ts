import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { IconButtonComponent } from '@app/shared/components/icon/icon-button.component';
import { IconButtonsComponent } from '@app/shared/components/icon/icon-buttons.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { RouterService } from '@app/shared/services/router.service';
import { AnalysisStrategyComponent } from '../../strategy/analysis-strategy.component';
import { AnalysisStrategyService } from '../../strategy/analysis-strategy.service';

@Component({
  selector: 'kpn-analysis-motorboat-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <nz-breadcrumb>
        <nz-breadcrumb-item>
          <a routerLink="/" i18n="@@breadcrumb.home">Home</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <span i18n="@@route-type.motorboat">Motorboat</span>
        </nz-breadcrumb-item>
      </nz-breadcrumb>

      <kpn-page-header>
        <span class="header-route-type-icon">
          <nz-icon [nzType]="'motorboat'" />
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
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
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
