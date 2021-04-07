import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { OnInit } from '@angular/core';
import { SubsetInfo } from '@api/common/subset/subset-info';
import { Subset } from '@api/custom/subset';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Util } from '../../../components/shared/util';
import { I18nService } from '../../../i18n/i18n.service';
import { SubsetCacheService } from '../../../services/subset-cache.service';

@Component({
  selector: 'kpn-subset-page-header-block',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li>
        <a [routerLink]="networkTypeLink()">
          <kpn-network-type-name
            [networkType]="subset.networkType"
          ></kpn-network-type-name>
        </a>
      </li>
      <li>
        <a [routerLink]="countryLink()">
          <kpn-country-name [country]="subset.country"></kpn-country-name>
        </a>
      </li>
      <li>
        <span
          *ngIf="pageName === 'networks'"
          i18n="@@subset-page.menu.networks"
        >
          Networks
        </span>
        <span *ngIf="pageName === 'facts'" i18n="@@subset-page.menu.facts">
          Facts
        </span>
        <span
          *ngIf="pageName === 'orphan-nodes'"
          i18n="@@subset-page.menu.orphan-nodes"
        >
          Orphan nodes
        </span>
        <span
          *ngIf="pageName === 'orphan-routes'"
          i18n="@@subset-page.menu.orphan-routes"
        >
          Orphan routes
        </span>
        <span *ngIf="pageName === 'map'" i18n="@@subset-page.menu.map">
          Map
        </span>
        <span *ngIf="pageName === 'changes'" i18n="@@subset-page.menu.changes">
          Changes
        </span>
      </li>
    </ul>

    <kpn-page-header
      [pageTitle]="subsetPageTitle()"
      [subject]="'subset-' + pageName + '-page'"
    >
      {{ subsetName() }}
    </kpn-page-header>

    <kpn-page-menu>
      <kpn-page-menu-option
        [link]="link('networks')"
        [active]="pageName === 'networks'"
        [elementCount]="networkCount$ | async"
        i18n="@@subset-page.menu.networks"
      >
        Networks
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('facts')"
        [active]="pageName === 'facts'"
        [elementCount]="factCount$ | async"
        i18n="@@subset-page.menu.facts"
      >
        Facts
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('orphan-nodes')"
        [active]="pageName === 'orphan-nodes'"
        [elementCount]="orphanNodeCount$ | async"
        i18n="@@subset-page.menu.orphan-nodes"
      >
        Orphan nodes
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('orphan-routes')"
        [active]="pageName === 'orphan-routes'"
        [elementCount]="orphanRouteCount$ | async"
        i18n="@@subset-page.menu.orphan-routes"
      >
        Orphan routes
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('map')"
        [active]="pageName === 'map'"
        i18n="@@subset-page.menu.map"
      >
        Map
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="link('changes')"
        [active]="pageName === 'changes'"
        i18n="@@subset-page.menu.changes"
      >
        Changes
      </kpn-page-menu-option>
    </kpn-page-menu>
  `,
})
export class SubsetPageHeaderBlockComponent implements OnInit {
  @Input() subset: Subset;
  @Input() pageName: string;
  @Input() pageTitle: string;
  @Input() subsetInfo$: Observable<SubsetInfo>;

  networkCount$: Observable<number>;
  factCount$: Observable<number>;
  orphanNodeCount$: Observable<number>;
  orphanRouteCount$: Observable<number>;

  constructor(
    private subsetCacheService: SubsetCacheService,
    private i18nService: I18nService
  ) {}

  ngOnInit(): void {
    this.networkCount$ = this.subsetInfo$.pipe(
      map((subsetInfo) => subsetInfo?.networkCount)
    );
    this.factCount$ = this.subsetInfo$.pipe(
      map((subsetInfo) => subsetInfo?.factCount)
    );
    this.orphanNodeCount$ = this.subsetInfo$.pipe(
      map((subsetInfo) => subsetInfo?.orphanNodeCount)
    );
    this.orphanRouteCount$ = this.subsetInfo$.pipe(
      map((subsetInfo) => subsetInfo?.orphanRouteCount)
    );
  }

  networkTypeLink(): string {
    const networkType = Util.safeGet(() => this.subset.networkType);
    return `/analysis/${networkType}`;
  }

  countryLink(): string {
    const networkType = Util.safeGet(() => this.subset.networkType);
    const country = Util.safeGet(() => this.subset.country);
    return `/analysis/${networkType}/${country}`;
  }

  link(targetPageName: string) {
    if (this.subset != null) {
      const networkType = Util.safeGet(() => this.subset.networkType);
      const country = Util.safeGet(() => this.subset.country);
      return `/analysis/${networkType}/${country}/${targetPageName}`;
    }
    return '/';
  }

  subsetName(): string {
    const networkType = this.i18nService.translation(
      '@@network-type.' + this.subset.networkType
    );
    const country = this.i18nService.translation(
      '@@country.' + this.subset.country
    );
    const inWord = this.i18nService.translation('@@subset.in');
    return `${networkType} ${inWord} ${country}`;
  }

  subsetPageTitle(): string {
    return this.subsetName() + ' | ' + this.pageTitle;
  }
}
