import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';

@Component({
  selector: 'ui-overview-page-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-breadcrumb>
      <nz-breadcrumb-item>
        <a routerLink="/" i18n="@@breadcrumb.home">Home</a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        <span i18n="@@breadcrumb.overview">Overview</span>
      </nz-breadcrumb-item>
    </nz-breadcrumb>
  `,
  imports: [RouterLink, NzBreadCrumbComponent, NzBreadCrumbItemComponent],
})
export class OverviewPageBreadcrumbComponent {}
