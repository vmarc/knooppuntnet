import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { I18nService } from '../../../i18n/i18n.service';
import { selectSubset } from '../store/subset.selectors';
import { selectSubsetInfo } from '../store/subset.selectors';

@Component({
  selector: 'kpn-subset-page-header-block',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-subset-page-breadcrumb
      [subset]="subset$ | async"
      [pageName]="pageName"
    ></kpn-subset-page-breadcrumb>

    <kpn-page-header
      [pageTitle]="subsetPageTitle$ | async"
      [subject]="'subset-' + pageName + '-page'"
    >
      <span class="header-network-type-icon">
        <mat-icon [svgIcon]="networkType$ | async"></mat-icon>
      </span>
      <span>
        {{ subsetName$ | async }}
      </span>
    </kpn-page-header>

    <kpn-subset-page-menu
      [subset]="subset$ | async"
      [subsetInfo]="subsetInfo$ | async"
      [pageName]="pageName"
    ></kpn-subset-page-menu>
  `,
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

  constructor(
    private store: Store<AppState>,
    private i18nService: I18nService
  ) {}
}
