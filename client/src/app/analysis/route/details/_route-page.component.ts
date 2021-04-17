import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { RouteDetailsPage } from '@api/common/route/route-details-page';
import { RouteInfoAnalysis } from '@api/common/route/route-info-analysis';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PageWidth } from '../../../components/shared/page-width';
import { PageWidthService } from '../../../components/shared/page-width.service';
import { InterpretedTags } from '../../../components/shared/tags/interpreted-tags';
import { AppState } from '../../../core/core.state';
import { FactInfo } from '../../fact/fact-info';
import { actionRouteDetailsPageInit } from '../store/route.actions';
import { selectRouteDetailsPage } from '../store/route.selectors';
import { selectRouteChangeCount } from '../store/route.selectors';
import { selectRouteName } from '../store/route.selectors';
import { selectRouteId } from '../store/route.selectors';

@Component({
  selector: 'kpn-route-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li i18n="@@breadcrumb.route-details">Route details</li>
    </ul>

    <kpn-route-page-header
      pageName="details"
      [routeId]="routeId$ | async"
      [routeName]="routeName$ | async"
      [changeCount]="changeCount$ | async"
    >
    </kpn-route-page-header>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result" i18n="@@route.route-not-found">
        Route not found
      </div>

      <div *ngIf="response.result as page">
        <kpn-data title="Summary" i18n-title="@@route.summary">
          <kpn-route-summary [route]="page.route"></kpn-route-summary>
        </kpn-data>

        <kpn-data title="Situation on" i18n-title="@@route.situation-on">
          <kpn-timestamp [timestamp]="response.situationOn"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Last updated" i18n-title="@@route.last-updated">
          <kpn-timestamp [timestamp]="page.route.lastUpdated"></kpn-timestamp>
        </kpn-data>

        <kpn-data
          title="Relation last updated"
          i18n-title="@@route.relation-last-updated"
        >
          <kpn-timestamp
            [timestamp]="page.route.summary.timestamp"
          ></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Network" i18n-title="@@route.network">
          <kpn-route-network-references
            [references]="page.references.networkReferences"
          >
          </kpn-route-network-references>
        </kpn-data>

        <div *ngIf="page.route.analysis as analysis">
          <kpn-data
            *ngIf="hasFreeNodes(analysis)"
            title="Nodes"
            i18n-title="@@route.nodes"
          >
            <kpn-route-free-nodes [analysis]="analysis"></kpn-route-free-nodes>
          </kpn-data>

          <kpn-data
            *ngIf="!hasFreeNodes(analysis)"
            title="Start node"
            i18n-title="@@route.start-node"
          >
            <kpn-route-start-nodes
              [analysis]="analysis"
            ></kpn-route-start-nodes>
          </kpn-data>

          <kpn-data
            *ngIf="!hasFreeNodes(analysis)"
            title="End node"
            i18n-title="@@route.end-node"
          >
            <kpn-route-end-nodes [analysis]="analysis"></kpn-route-end-nodes>
          </kpn-data>

          <div *ngIf="analysis.map.redundantNodes.length > 0">
            <kpn-data
              title="Redundant node"
              i18n-title="@@route.redundant-node"
            >
              <kpn-route-redundant-nodes
                [analysis]="analysis"
              ></kpn-route-redundant-nodes>
            </kpn-data>
          </div>

          <kpn-data title="Number of ways" i18n-title="@@route.number-of-ways">
            {{ page.route.summary.wayCount }}
          </kpn-data>
        </div>

        <kpn-data title="Tags" i18n-title="@@route.tags">
          <kpn-tags-table [tags]="routeTags(page)"></kpn-tags-table>
        </kpn-data>

        <kpn-data title="Location" i18n-title="@@route.location">
          <kpn-route-location
            [locationAnalysis]="page.route.analysis.locationAnalysis"
          ></kpn-route-location>
        </kpn-data>

        <div *ngIf="page.route.analysis && showRouteDetails$ | async">
          <kpn-data title="Structure" i18n-title="@@route.structure">
            <kpn-route-structure
              [structureStrings]="page.route.analysis.structureStrings"
            ></kpn-route-structure>
          </kpn-data>
        </div>

        <kpn-data title="Facts" i18n-title="@@route.facts">
          <kpn-facts [factInfos]="factInfos(page)"></kpn-facts>
        </kpn-data>

        <div *ngIf="showRouteDetails$ | async">
          <kpn-route-members
            [networkType]="page.route.summary.networkType"
            [members]="page.route.analysis.members"
          >
          </kpn-route-members>
        </div>
      </div>
    </div>
  `,
})
export class RoutePageComponent implements OnInit {
  readonly routeId$ = this.store.select(selectRouteId);
  readonly routeName$ = this.store.select(selectRouteName);
  readonly changeCount$ = this.store.select(selectRouteChangeCount);
  readonly response$ = this.store.select(selectRouteDetailsPage);

  showRouteDetails$: Observable<boolean>;

  constructor(
    private pageWidthService: PageWidthService,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
    this.store.dispatch(actionRouteDetailsPageInit());
    this.showRouteDetails$ = this.pageWidthService.current$.pipe(
      map(
        (pageWidth) =>
          pageWidth !== PageWidth.small &&
          pageWidth !== PageWidth.verySmall &&
          pageWidth !== PageWidth.veryVerySmall
      )
    );
  }

  routeTags(page: RouteDetailsPage) {
    return InterpretedTags.routeTags(page.route.tags);
  }

  factInfos(page: RouteDetailsPage): FactInfo[] {
    return page.route.facts.map((fact) => new FactInfo(fact));
  }

  hasFreeNodes(analysis: RouteInfoAnalysis): boolean {
    return analysis.map.freeNodes && analysis.map.freeNodes.length > 0;
  }
}
