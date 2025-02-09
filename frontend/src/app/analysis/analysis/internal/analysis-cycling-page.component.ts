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
  selector: 'kpn-analysis-cycling-page',
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
          <span i18n="@@route-type.cycling">Cycling</span>
        </nz-breadcrumb-item>
      </nz-breadcrumb>

      <kpn-page-header>
        <span class="header-route-type-icon">
          <nz-icon nzType="cycling" />
        </span>
        <span i18n="@@route-type.cycling">Cycling</span>
      </kpn-page-header>

      <kpn-analysis-strategy />

      <kpn-icon-buttons>
        <kpn-icon-button
          [routerLink]="nlLink()"
          icon="netherlands"
          i18n-title="@@country.nl"
          title="The Netherlands"
        />
        <kpn-icon-button
          [routerLink]="beLink()"
          icon="belgium"
          i18n-title="@@country.be"
          title="Belgium"
        />
        <kpn-icon-button
          [routerLink]="deLink()"
          icon="germany"
          i18n-title="@@country.de"
          title="Germany"
        />
        <kpn-icon-button
          [routerLink]="frLink()"
          icon="france"
          i18n-title="@@country.fr"
          title="France"
        />
        <kpn-icon-button
          [routerLink]="atLink()"
          icon="austria"
          i18n-title="@@country.at"
          title="Austria"
        />
        <kpn-icon-button
          [routerLink]="esLink()"
          icon="spain"
          i18n-title="@@country.es"
          title="Spain"
        />
        <kpn-icon-button
          [routerLink]="dkLink()"
          icon="denmark"
          i18n-title="@@country.dk"
          title="Denmark"
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
export class AnalysisCyclingPageComponent implements OnInit {
  private readonly analysisStrategyService = inject(AnalysisStrategyService);
  protected readonly nlLink = this.analysisStrategyService.link('cycling', 'nl');
  protected readonly beLink = this.analysisStrategyService.link('cycling', 'be');
  protected readonly deLink = this.analysisStrategyService.link('cycling', 'de');
  protected readonly frLink = this.analysisStrategyService.link('cycling', 'fr');
  protected readonly atLink = this.analysisStrategyService.link('cycling', 'at');
  protected readonly esLink = this.analysisStrategyService.link('cycling', 'es');
  protected readonly dkLink = this.analysisStrategyService.link('cycling', 'dk');

  ngOnInit(): void {
    this.analysisStrategyService.init();
  }
}
