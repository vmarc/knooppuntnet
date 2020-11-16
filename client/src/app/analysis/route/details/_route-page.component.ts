import {ChangeDetectionStrategy} from '@angular/core';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {List} from 'immutable';
import {Subject} from 'rxjs';
import {Observable} from 'rxjs';
import {map, mergeMap, tap} from 'rxjs/operators';
import {AppService} from '../../../app.service';
import {PageWidth} from '../../../components/shared/page-width';
import {PageWidthService} from '../../../components/shared/page-width.service';
import {PageService} from '../../../components/shared/page.service';
import {InterpretedTags} from '../../../components/shared/tags/interpreted-tags';
import {RouteDetailsPage} from '../../../kpn/api/common/route/route-details-page';
import {RouteInfo} from '../../../kpn/api/common/route/route-info';
import {ApiResponse} from '../../../kpn/api/custom/api-response';
import {FactInfo} from '../../fact/fact-info';
import {AppState} from '../../../core/core.state';
import {Store} from '@ngrx/store';
import {actionPreferencesNetworkType} from '../../../core/preferences/preferences.actions';

@Component({
  selector: 'kpn-route-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a></li>
      <li i18n="@@breadcrumb.route-details">Route details</li>
    </ul>

    <kpn-route-page-header
      pageName="details"
      [routeId]="routeId$ | async"
      [routeName]="routeName$ | async"
      [changeCount]="changeCount$ | async">
    </kpn-route-page-header>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">

      <div *ngIf="!response.result" i18n="@@route.route-not-found">
        Route not found
      </div>

      <div *ngIf="response.result">

        <kpn-data title="Summary" i18n-title="@@route.summary">
          <kpn-route-summary [route]="route"></kpn-route-summary>
        </kpn-data>

        <kpn-data title="Situation on" i18n-title="@@route.situation-on">
          <kpn-timestamp [timestamp]="response.situationOn"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Last updated" i18n-title="@@route.last-updated">
          <kpn-timestamp [timestamp]="route.lastUpdated"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Relation last updated" i18n-title="@@route.relation-last-updated">
          <kpn-timestamp [timestamp]="route.summary.timestamp"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Network" i18n-title="@@route.network">
          <kpn-route-network-references
            [references]="response.result.references.networkReferences">
          </kpn-route-network-references>
        </kpn-data>

        <div *ngIf="route.analysis">

          <kpn-data title="Start node" i18n-title="@@route.start-node">
            <kpn-route-start-nodes [analysis]="route.analysis"></kpn-route-start-nodes>
          </kpn-data>

          <kpn-data title="End node" i18n-title="@@route.end-node">
            <kpn-route-end-nodes [analysis]="route.analysis"></kpn-route-end-nodes>
          </kpn-data>

          <div *ngIf="!route.analysis.map.redundantNodes.isEmpty()">
            <kpn-data title="Redundant node" i18n-title="@@route.redundant-node">
              <kpn-route-redundant-nodes [analysis]="route.analysis"></kpn-route-redundant-nodes>
            </kpn-data>
          </div>

          <kpn-data title="Number of ways" i18n-title="@@route.number-of-ways">
            {{route.summary.wayCount}}
          </kpn-data>
        </div>

        <kpn-data title="Tags" i18n-title="@@route.tags">
          <kpn-tags-table [tags]="tags"></kpn-tags-table>
        </kpn-data>

        <kpn-data title="Location" i18n-title="@@route.location">
          <kpn-route-location [locationAnalysis]="route.analysis.locationAnalysis"></kpn-route-location>
        </kpn-data>

        <div *ngIf="route.analysis && showRouteDetails$ | async">
          <kpn-data title="Structure" i18n-title="@@route.structure">
            <kpn-route-structure [structureStrings]="route.analysis.structureStrings"></kpn-route-structure>
          </kpn-data>
        </div>

        <kpn-data title="Facts" i18n-title="@@route.facts">
          <kpn-facts [factInfos]="factInfos"></kpn-facts>
        </kpn-data>

        <div *ngIf="showRouteDetails$ | async">
          <kpn-route-members
            [networkType]="route.summary.networkType"
            [members]="route.analysis.members">
          </kpn-route-members>
        </div>
      </div>
    </div>
  `
})
export class RoutePageComponent implements OnInit {

  response$: Observable<ApiResponse<RouteDetailsPage>>;
  showRouteDetails$: Observable<boolean>;

  routeId$ = new Subject<string>();
  routeName$ = new Subject<string>();
  changeCount$ = new Subject<number>();
  route: RouteInfo;
  tags: InterpretedTags;
  factInfos: List<FactInfo>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private pageWidthService: PageWidthService,
              private store: Store<AppState>) {
  }

  ngOnInit(): void {
    this.showRouteDetails$ = this.pageWidthService.current$.pipe(
      map(pageWidth => pageWidth !== PageWidth.small && pageWidth !== PageWidth.verySmall && pageWidth !== PageWidth.veryVerySmall)
    );
    this.routeName$.next(history.state.routeName);
    this.changeCount$.next(history.state.changeCount);
    this.response$ = this.activatedRoute.params.pipe(
      map(params => params['routeId']),
      tap(routeId => this.routeId$.next(routeId)),
      mergeMap(routeId => this.appService.routeDetails(routeId)),
      tap(response => {
        if (response.result) {
          this.route = response.result.route;
          this.routeName$.next(this.route.summary.name);
          this.changeCount$.next(response.result.changeCount);
          this.tags = InterpretedTags.routeTags(this.route.tags);
          this.factInfos = this.route.facts.map(fact => new FactInfo(fact));
          this.store.dispatch(actionPreferencesNetworkType({networkType: this.route.summary.networkType.name}));
        }
      })
    );
  }
}
