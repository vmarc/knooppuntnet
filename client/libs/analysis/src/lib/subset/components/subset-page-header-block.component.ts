import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { PageHeaderComponent } from '@app/components/shared/page';
import { I18nService } from '@app/i18n';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { selectSubset } from '../store/subset.selectors';
import { selectSubsetInfo } from '../store/subset.selectors';
import { SubsetPageBreadcrumbComponent } from './subset-page-breadcrumb.component';
import { SubsetPageMenuComponent } from './subset-page-menu.component';

@Component({
  selector: 'kpn-subset-page-header-block',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-subset-page-breadcrumb
      [subset]="subset$ | async"
      [pageName]="pageName"
    />

    <kpn-page-header
      [pageTitle]="subsetPageTitle$ | async"
      [subject]="'subset-' + pageName + '-page'"
    >
      <span class="header-network-type-icon">
        <mat-icon [svgIcon]="networkType$ | async" />
      </span>
      <span>
        {{ subsetName$ | async }}
      </span>
    </kpn-page-header>

    <kpn-subset-page-menu
      [subset]="subset$ | async"
      [subsetInfo]="subsetInfo$ | async"
      [pageName]="pageName"
    />
  `,
  standalone: true,
  imports: [
    SubsetPageBreadcrumbComponent,
    PageHeaderComponent,
    MatIconModule,
    SubsetPageMenuComponent,
    AsyncPipe,
  ],
})
export class SubsetPageHeaderBlockComponent {
  @Input() pageName: string;
  @Input() pageTitle: string;

  readonly subset$ = this.store.select(selectSubset);
  readonly subsetInfo$ = this.store.select(selectSubsetInfo);
  readonly networkType$ = this.subset$.pipe(map((s) => s.networkType));

  readonly subsetName$ = this.subset$.pipe(
    map((subset) => {
      const networkType = this.i18nService.translation(
        '@@network-type.' + subset.networkType
      );
      const country = this.i18nService.translation(
        '@@country.' + subset.country
      );
      const inWord = this.i18nService.translation('@@subset.in');
      return `${networkType} ${inWord} ${country}`;
    })
  );

  subsetPageTitle$ = this.subsetName$.pipe(
    map((subsetName) => subsetName + ' | ' + this.pageTitle)
  );

  constructor(private store: Store, private i18nService: I18nService) {}
}
