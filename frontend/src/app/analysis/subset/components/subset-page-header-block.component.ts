import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { PageHeaderComponent } from '@app/components/shared/page';
import { Translations } from '@app/i18n';
import { SubsetStore } from '../subset.store';
import { SubsetPageBreadcrumbComponent } from './subset-page-breadcrumb.component';
import { SubsetPageMenuComponent } from './subset-page-menu.component';

@Component({
  selector: 'kpn-subset-page-header-block',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-subset-page-breadcrumb [subset]="subset()" [pageName]="pageName()" />

    <kpn-page-header [pageTitle]="subsetPageTitle()" [subject]="'subset-' + pageName() + '-page'">
      <span class="header-network-type-icon">
        <mat-icon [svgIcon]="networkType()" />
      </span>
      <span>
        {{ subsetName() }}
      </span>
    </kpn-page-header>

    <kpn-subset-page-menu [subset]="subset()" [subsetInfo]="subsetInfo()" [pageName]="pageName()" />
  `,
  standalone: true,
  imports: [
    MatIconModule,
    PageHeaderComponent,
    SubsetPageBreadcrumbComponent,
    SubsetPageMenuComponent,
  ],
})
export class SubsetPageHeaderBlockComponent {
  pageName = input.required<string>();
  pageTitle = input.required<string>();

  private readonly store = inject(SubsetStore);

  protected readonly subset = this.store.subset;
  protected readonly subsetInfo = this.store.subsetInfo;
  protected readonly networkType = computed(() => this.subset().networkType);

  protected readonly subsetName = computed(() => {
    const ss = this.subset();
    const networkType = Translations.get('network-type.' + ss.networkType);
    const country = Translations.get('country.' + ss.country);
    const inWord = Translations.get('subset.in');
    return `${networkType} ${inWord} ${country}`;
  });

  protected readonly subsetPageTitle = computed(() => `${this.subsetName()} | ${this.pageTitle()}`);
}
