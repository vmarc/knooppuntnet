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
  selector: 'kpn-analysis-cycling-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li i18n="@@route-type.cycling">Cycling</li>
      </ul>

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
