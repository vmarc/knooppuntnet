import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AnalysisStrategyComponent } from '@app/analysis/strategy/analysis-strategy.component';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { IconButtonComponent } from '@app/shared/components/icon/icon-button.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { RouterService } from '../../../shared/services/router.service';

@Component({
  selector: 'kpn-analysis-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <nz-breadcrumb>
        <nz-breadcrumb-item>
          <a routerLink="/" i18n="@@breadcrumb.home">Home</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <span i18n="@@breadcrumb.analysis">Analysis</span>
        </nz-breadcrumb-item>
      </nz-breadcrumb>

      <kpn-page-header i18n="@@analysis-page.title">Analysis</kpn-page-header>

      <kpn-analysis-strategy />

      <div class="buttons">
        <kpn-icon-button
          routerLink="/analysis/overview"
          icon="overview"
          i18n-title="@@analysis-page.overview"
          title="Overview"
        />
        <kpn-icon-button
          routerLink="/analysis/changes"
          icon="changes"
          i18n-title="@@analysis-page.changes"
          title="Changes"
        />
      </div>

      <div class="buttons">
        <kpn-icon-button
          routerLink="/analysis/cycling"
          icon="cycling"
          i18n-title="@@route-type.cycling"
          title="Cycling"
        />
        <kpn-icon-button
          routerLink="/analysis/hiking"
          icon="hiking"
          i18n-title="@@route-type.hiking"
          title="Hiking"
        />
        <kpn-icon-button
          routerLink="/analysis/horse-riding"
          icon="horse-riding"
          i18n-title="@@route-type.horse-riding"
          title="Horse riding"
        />
        <kpn-icon-button
          routerLink="/analysis/motorboat"
          icon="motorboat"
          i18n-title="@@route-type.motorboat"
          title="Motorboat"
        />
        <kpn-icon-button
          routerLink="/analysis/canoe"
          icon="canoe"
          i18n-title="@@route-type.canoe"
          title="Canoe"
        />
        <kpn-icon-button
          routerLink="/analysis/inline-skating"
          icon="inline-skating"
          i18n-title="@@route-type.inline-skating"
          title="Inline skating"
        />
      </div>
    </kpn-page>
  `,
  styles: `
    .buttons {
      display: flex;
      flex-wrap: wrap;
    }
  `,
  providers: [AnalysisStrategyService, RouterService],
  imports: [
    AnalysisStrategyComponent,
    IconButtonComponent,
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class AnalysisPageComponent implements OnInit {
  private analysisStrategyService = inject(AnalysisStrategyService);

  ngOnInit(): void {
    this.analysisStrategyService.init();
  }
}
