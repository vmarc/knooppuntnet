import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Translations } from '@app/i18n';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { SubsetService } from '../subset.service';
import { SubsetPageBreadcrumbComponent } from './subset-page-breadcrumb.component';
import { SubsetPageMenuComponent } from './subset-page-menu.component';

@Component({
  selector: 'kpn-subset-page-header-block',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-subset-page-breadcrumb [subset]="subset()" [pageName]="pageName()" />

    <kpn-page-header [pageTitle]="subsetPageTitle()" [subject]="'subset-' + pageName() + '-page'">
      <span class="header-route-type-icon">
        <nz-icon [nzType]="routeType()" />
      </span>
      <span>
        {{ subsetName() }}
      </span>
    </kpn-page-header>

    <kpn-subset-page-menu [subset]="subset()" [subsetInfo]="subsetInfo()" [pageName]="pageName()" />
  `,
  imports: [
    NzIconDirective,
    PageHeaderComponent,
    SubsetPageBreadcrumbComponent,
    SubsetPageMenuComponent,
  ],
})
export class SubsetPageHeaderBlockComponent {
  pageName = input.required<string>();
  pageTitle = input.required<string>();

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
}
