import { AsyncPipe } from '@angular/common';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { PageHeaderComponent } from '@app/components/shared/page';
import { I18nService } from '@app/i18n';
import { Store } from '@ngrx/store';
import { selectSubset } from '../store/subset.selectors';
import { selectSubsetInfo } from '../store/subset.selectors';
import { SubsetPageBreadcrumbComponent } from './subset-page-breadcrumb.component';
import { SubsetPageMenuComponent } from './subset-page-menu.component';

@Component({
  selector: 'kpn-subset-page-header-block',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-subset-page-breadcrumb [subset]="subset()" [pageName]="pageName" />

    <kpn-page-header
      [pageTitle]="subsetPageTitle()"
      [subject]="'subset-' + pageName + '-page'"
    >
      <span class="header-network-type-icon">
        <mat-icon [svgIcon]="networkType()" />
      </span>
      <span>
        {{ subsetName() }}
      </span>
    </kpn-page-header>

    <kpn-subset-page-menu
      [subset]="subset()"
      [subsetInfo]="subsetInfo()"
      [pageName]="pageName"
    />
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    MatIconModule,
    PageHeaderComponent,
    SubsetPageBreadcrumbComponent,
    SubsetPageMenuComponent,
  ],
})
export class SubsetPageHeaderBlockComponent {
  @Input() pageName: string;
  @Input() pageTitle: string;

  readonly subset = this.store.selectSignal(selectSubset);
  readonly subsetInfo = this.store.selectSignal(selectSubsetInfo);
  readonly networkType = computed(() => this.subset().networkType);

  readonly subsetName = computed(() => {
    const ss = this.subset();
    const networkType = this.i18nService.translation(
      '@@network-type.' + ss.networkType
    );
    const country = this.i18nService.translation('@@country.' + ss.country);
    const inWord = this.i18nService.translation('@@subset.in');
    return `${networkType} ${inWord} ${country}`;
  });

  readonly subsetPageTitle = computed(
    () => `${this.subsetName()} | ${this.pageTitle}`
  );

  constructor(
    private store: Store,
    private i18nService: I18nService
  ) {}
}
