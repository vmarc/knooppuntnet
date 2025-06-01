import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { IconButtonComponent } from '@app/shared/components/icon/icon-button.component';
import { IconButtonsComponent } from '@app/shared/components/icon/icon-buttons.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { RouterService } from '@app/shared/services/router.service';
import { AnalysisStrategyComponent } from '../../strategy/analysis-strategy.component';
import { AnalysisStrategyService } from '../../strategy/analysis-strategy.service';

@Component({
  selector: 'ui-analysis-canoe-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
      <ui-page-header>
        <span class="header-route-type-icon">
          <nz-icon nzType="canoe" />
        </span>
        <span i18n="@@route-type.canoe">Canoe</span>
      </ui-page-header>

      <ui-analysis-strategy />

      <ui-icon-buttons>
        <ui-icon-button
          [routerLink]="nlLink()"
          icon="netherlands"
          i18n-title="@@country.nl"
          title="The Netherlands"
        />
        <ui-icon-button
          [routerLink]="frLink()"
          icon="france"
          i18n-title="@@country.fr"
          title="France"
        />
      </ui-icon-buttons>
    </ui-page>
  `,
  providers: [AnalysisStrategyService, RouterService],
  imports: [
    AnalysisStrategyComponent,
    BreadcrumbComponent,
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

  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    Breadcrumbs.analysis,
    { label: $localize`:@@route-type.canoe:Canoe` },
  ];

  ngOnInit(): void {
    this.analysisStrategyService.init();
  }
}
