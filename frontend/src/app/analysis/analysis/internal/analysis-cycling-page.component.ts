import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Country } from '@api/common/country';
import { Translations } from '@app/shared/i18n/translations';
import { AnalysisCountryLinkComponent } from './analysis-country-link.component';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { RouterService } from '@app/shared/services/router.service';
import { AnalysisStrategyComponent } from '../../strategy/analysis-strategy.component';
import { AnalysisStrategyService } from '../../strategy/analysis-strategy.service';

@Component({
  selector: 'ui-analysis-cycling-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
      <ui-page-header>
        <span class="header-route-type-icon">
          <nz-icon nzType="cycling" />
        </span>
        <span i18n="@@route-type.cycling">Cycling</span>
      </ui-page-header>

      <ui-analysis-strategy />

      <span>Country</span>

      <ul>
        @for (country of countries; track country) {
          <ui-analysis-country-link routeType="cycling" [country]="country" />
        }
      </ul>
    </ui-page>
  `,
  providers: [AnalysisStrategyService, RouterService],
  imports: [
    AnalysisCountryLinkComponent,
    AnalysisStrategyComponent,
    BreadcrumbComponent,
    NzIconDirective,
    PageComponent,
    PageHeaderComponent,
  ],
})
export class AnalysisCyclingPageComponent implements OnInit {
  private readonly analysisStrategyService = inject(AnalysisStrategyService);
  protected readonly countries: Country[] = ['nl', 'be', 'de', 'fr', 'at', 'es', 'dk', 'pl'];
  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    Breadcrumbs.analysis,
    { label: Translations.routeTypeCycling },
  ];

  ngOnInit(): void {
    this.analysisStrategyService.init();
  }
}
