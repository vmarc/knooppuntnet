import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AnalysisStrategyComponent } from '@app/analysis/strategy/analysis-strategy.component';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { IconButtonComponent } from '@app/shared/components/icon/icon-button.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';

@Component({
  selector: 'ui-analysis-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
      <ui-page-header i18n="@@analysis-page.title">Analysis</ui-page-header>
      <ui-analysis-strategy />

      <div class="buttons">
        <ui-icon-button
          routerLink="/analysis/overview"
          icon="overview"
          i18n-title="@@analysis-page.overview"
          title="Overview"
        />
        <ui-icon-button
          routerLink="/analysis/changes"
          icon="changes"
          i18n-title="@@analysis-page.changes"
          title="Changes"
        />
      </div>

      <div class="buttons">
        <ui-icon-button
          routerLink="/analysis/cycling"
          icon="cycling"
          i18n-title="@@route-type.cycling"
          title="Cycling"
        />
        <ui-icon-button
          routerLink="/analysis/hiking"
          icon="hiking"
          i18n-title="@@route-type.hiking"
          title="Hiking"
        />
        <ui-icon-button
          routerLink="/analysis/horse-riding"
          icon="horse-riding"
          i18n-title="@@route-type.horse-riding"
          title="Horse riding"
        />
        <ui-icon-button
          routerLink="/analysis/motorboat"
          icon="motorboat"
          i18n-title="@@route-type.motorboat"
          title="Motorboat"
        />
        <ui-icon-button
          routerLink="/analysis/canoe"
          icon="canoe"
          i18n-title="@@route-type.canoe"
          title="Canoe"
        />
        <ui-icon-button
          routerLink="/analysis/inline-skating"
          icon="inline-skating"
          i18n-title="@@route-type.inline-skating"
          title="Inline skating"
        />
      </div>
    </ui-page>
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
    BreadcrumbComponent,
    IconButtonComponent,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class AnalysisPageComponent implements OnInit {
  private analysisStrategyService = inject(AnalysisStrategyService);

  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    { label: Breadcrumbs.analysisLabel },
  ];

  ngOnInit(): void {
    this.analysisStrategyService.init();
  }
}
