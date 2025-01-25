import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { IconButtonComponent } from '@app/components/shared/icon';
import { IconButtonsComponent } from '@app/components/shared/icon';
import { OldPageComponent } from '@app/components/shared/page';
import { PageHeaderComponent } from '@app/components/shared/page';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { RouterService } from '../../../shared/services/router.service';
import { AnalysisStrategyService } from '../../strategy';
import { AnalysisSidebarComponent } from '../analysis-sidebar.component';

@Component({
  selector: 'kpn-analysis-horse-riding-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-old-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li i18n="@@route-type.horse-riding">Horse riding</li>
      </ul>

      <kpn-page-header>
        <span class="header-route-type-icon">
          <nz-icon nzType="horse-riding" />
        </span>
        <span i18n="@@route-type.horse-riding">Horse riding</span>
      </kpn-page-header>

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
          [routerLink]="frLink()"
          icon="france"
          i18n-title="@@country.fr"
          title="France"
        />
      </kpn-icon-buttons>
      <kpn-analysis-sidebar sidebar />
    </kpn-old-page>
  `,
  providers: [AnalysisStrategyService, RouterService],
  imports: [
    AnalysisSidebarComponent,
    IconButtonComponent,
    IconButtonsComponent,
    MatIconModule,
    NzIconDirective,
    OldPageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class AnalysisHorseRidingPageComponent implements OnInit {
  private readonly analysisStrategyService = inject(AnalysisStrategyService);
  protected readonly nlLink = this.analysisStrategyService.link('horse-riding', 'nl');
  protected readonly beLink = this.analysisStrategyService.link('horse-riding', 'be');
  protected readonly frLink = this.analysisStrategyService.link('horse-riding', 'fr');

  ngOnInit(): void {
    this.analysisStrategyService.init();
  }
}
