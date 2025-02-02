import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { IconButtonComponent } from '@app/components/shared/icon';
import { IconButtonsComponent } from '@app/components/shared/icon';
import { PageHeaderComponent } from '@app/components/shared/page';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { PageComponent } from '../../../shared/components/shared/page/page.component';
import { RouterService } from '../../../shared/services/router.service';
import { AnalysisStrategyComponent } from '../../strategy';
import { AnalysisStrategyService } from '../../strategy';

@Component({
  selector: 'kpn-analysis-horse-riding-page',
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
          <span i18n="@@route-type.horse-riding">Horse riding</span>
        </nz-breadcrumb-item>
      </nz-breadcrumb>

      <kpn-page-header>
        <span class="header-route-type-icon">
          <nz-icon nzType="horse-riding" />
        </span>
        <span i18n="@@route-type.horse-riding">Horse riding</span>
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
          [routerLink]="frLink()"
          icon="france"
          i18n-title="@@country.fr"
          title="France"
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
export class AnalysisHorseRidingPageComponent implements OnInit {
  private readonly analysisStrategyService = inject(AnalysisStrategyService);
  protected readonly nlLink = this.analysisStrategyService.link('horse-riding', 'nl');
  protected readonly beLink = this.analysisStrategyService.link('horse-riding', 'be');
  protected readonly frLink = this.analysisStrategyService.link('horse-riding', 'fr');

  ngOnInit(): void {
    this.analysisStrategyService.init();
  }
}
