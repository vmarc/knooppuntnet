import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { SubsetPageName } from '@app/analysis/subset/internal/components/subset-page-name';
import { Translations } from '@app/shared/i18n/translations';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { SubsetService } from '../subset.service';
import { SubsetPageBreadcrumbComponent } from './subset-page-breadcrumb.component';
import { SubsetPageMenuComponent } from './subset-page-menu.component';

@Component({
  selector: 'ui-subset-page-header-block',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-subset-page-breadcrumb [subset]="subset()" [pageName]="pageName()" />

    <ui-page-header [pageTitle]="subsetPageTitle()" [subject]="subject()">
      <span class="header-route-type-icon">
        <nz-icon [nzType]="routeType()" />
      </span>
      <span>
        {{ subsetName() }}
      </span>
    </ui-page-header>

    <ui-subset-page-menu [subset]="subset()" [subsetInfo]="subsetInfo()" [pageName]="pageName()" />
  `,
  imports: [
    NzIconDirective,
    PageHeaderComponent,
    SubsetPageBreadcrumbComponent,
    SubsetPageMenuComponent,
  ],
})
export class SubsetPageHeaderBlockComponent {
  readonly pageName = input.required<SubsetPageName>();
  readonly pageTitle = input.required<string>();

  private readonly service = inject(SubsetService);

  protected readonly subset = this.service.subset;
  protected readonly subsetInfo = this.service.subsetInfo;
  protected readonly routeType = computed(() => this.subset()?.routeType);

  protected readonly subsetName = computed(() => {
    const ss = this.subset();
    const routeType = Translations.get('route-type.' + ss.routeType);
    const country = Translations.get('country.' + ss.country);
    const inWord = Translations.get('subset.in');
    return `${routeType} ${inWord} ${country}`;
  });

  protected readonly subsetPageTitle = computed(() => `${this.subsetName()} | ${this.pageTitle()}`);
  protected readonly subject = computed(() => `subset-${this.pageName()}-page`);
}
