import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { ChangeListComponent } from '@app/analysis/changes/internal/components/change-list.component';
import { AnalysisStrategyComponent } from '@app/analysis/strategy/analysis-strategy.component';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { NzDividerComponent } from 'ng-zorro-antd/divider';
import { RouterService } from '@app/shared/services/router.service';
import { UserLinkLoginComponent } from '@app/shared/user/user-link-login.component';
import { ChangesPageService } from './changes-page.service';

@Component({
  selector: 'ui-changes-page',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
      <ui-page-header subject="changes-page" i18n="@@changes-page.title"> Changes </ui-page-header>

      <ui-analysis-strategy (strategyChange)="onStrategyChange()" />
      <nz-divider />
      <ui-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!service.loggedIn()) {
            <p i18n="@@changes-page.login-required">
              The details of the changes history are available to logged in OpenStreetMap
              contributors only.
            </p>
            <p>
              <ui-user-link-login />
            </p>
          } @else {
            @if (response.result; as page) {
              <ui-change-list />
            }
          }
        </div>
      }
    </ui-page>
  `,
  providers: [ChangesPageService, AnalysisStrategyService, RouterService],
  imports: [
    AnalysisStrategyComponent,
    BreadcrumbComponent,
    ChangeListComponent,
    ErrorComponent,
    NzDividerComponent,
    PageComponent,
    PageHeaderComponent,
    UserLinkLoginComponent,
  ],
})
export class ChangesPageComponent implements OnInit {
  protected readonly service = inject(ChangesPageService);

  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    Breadcrumbs.analysis,
    { label: Breadcrumbs.changesLabel },
  ];

  ngOnInit(): void {
    this.service.onInit();
  }

  onStrategyChange(): void {
    this.service.strategyUpdated();
  }
}
