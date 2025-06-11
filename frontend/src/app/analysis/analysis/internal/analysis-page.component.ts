import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { RouteType } from '@api/common/route-type';
import { AnalysisLinkComponent } from './analysis-link.component';
import { AnalysisStrategyComponent } from '@app/analysis/strategy/analysis-strategy.component';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-analysis-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
      <ui-page-header i18n="@@analysis-page.title">Analysis</ui-page-header>

      <a routerLink="/analysis/overview">
        <nz-icon nzType="file-text" class="icon" />
        <span i18n="@@analysis-page.overview">Overview</span>
      </a>

      <a routerLink="/analysis/changes">
        <nz-icon nzType="clock-circle" />
        <span i18n="@@analysis-page.changes">Changes</span>
      </a>

      <ui-analysis-strategy />

      @for (routeType of routeTypes; track routeType) {
        <ui-analysis-link [routeType]="routeType" />
      }
    </ui-page>
  `,
  styles: `
    a {
      display: block;
      margin-top: 0.5em;
      margin-bottom: 0.5em;
    }

    a > nz-icon {
      padding-right: 1em;
    }
  `,
  providers: [AnalysisStrategyService, RouterService],
  imports: [
    AnalysisLinkComponent,
    AnalysisStrategyComponent,
    BreadcrumbComponent,
    NzIconDirective,
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

  protected readonly routeTypes: RouteType[] = [
    'cycling',
    'hiking',
    'horse-riding',
    'motorboat',
    'canoe',
    'inline-skating',
  ];

  ngOnInit(): void {
    this.analysisStrategyService.init();
  }
}
