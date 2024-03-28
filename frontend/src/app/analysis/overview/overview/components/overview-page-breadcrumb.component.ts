import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'kpn-overview-page-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li i18n="@@breadcrumb.overview">Overview</li>
    </ul>
  `,
  standalone: true,
  imports: [RouterLink],
})
export class OverviewPageBreadcrumbComponent {}
