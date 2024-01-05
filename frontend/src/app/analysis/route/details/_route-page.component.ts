import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { RouteDetailsPage } from '@api/common/route';
import { RouteInfoAnalysis } from '@api/common/route';
import { FactInfo } from '@app/analysis/fact';
import { FactsComponent } from '@app/analysis/fact';
import { PageWidth } from '@app/components/shared';
import { PageWidthService } from '@app/components/shared';
import { DataComponent } from '@app/components/shared/data';
import { PageComponent } from '@app/components/shared/page';
import { AnalysisSidebarComponent } from '@app/components/shared/sidebar';
import { InterpretedTags } from '@app/components/shared/tags';
import { TagsTableComponent } from '@app/components/shared/tags';
import { TimestampComponent } from '@app/components/shared/timestamp';
import { SymbolComponent } from '@app/symbol';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';
import { actionRouteDetailsPageDestroy } from '../store/route.actions';
import { actionRouteDetailsPageInit } from '../store/route.actions';
import { selectRouteNetworkType } from '../store/route.selectors';
import { selectRouteDetailsPage } from '../store/route.selectors';
import { selectRouteChangeCount } from '../store/route.selectors';
import { selectRouteName } from '../store/route.selectors';
import { selectRouteId } from '../store/route.selectors';
import { RouteEndNodesComponent } from './route-end-nodes.component';
import { RouteFreeNodesComponent } from './route-free-nodes.component';
import { RouteLocationComponent } from './route-location.component';
import { RouteMembersComponent } from './route-members.component';
import { RouteNetworkReferencesComponent } from './route-network-references.component';
import { RouteRedundantNodesComponent } from './route-redundant-nodes.component';
import { RouteStartNodesComponent } from './route-start-nodes.component';
import { RouteStructureComponent } from './route-structure.component';
import { RouteSummaryComponent } from './route-summary.component';

@Component({
  selector: 'kpn-route-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li i18n="@@breadcrumb.route-details">Route details</li>
      </ul>

      <kpn-route-page-header
        pageName="details"
        [routeId]="routeId()"
        [routeName]="routeName()"
        [changeCount]="changeCount()"
        [networkType]="networkType()"
      />

      @if (apiResponse(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <div i18n="@@route.route-not-found">Route not found</div>
          }
          @if (response.result; as page) {
            <div>
              <kpn-data title="Summary" i18n-title="@@route.summary">
                <kpn-route-summary [route]="page.route" />
              </kpn-data>
              @if (hasSymbol(page)) {
                <kpn-data title="Symbol" i18n-title="@@route.symbol">
                  <kpn-symbol [description]="symbolDescription(page)" />
                </kpn-data>
              }
              <div class="data2">
                <div class="title">
                  <span i18n="@@route.situation-on">Situation on</span>
                </div>
                <div class="body">
                  <kpn-timestamp [timestamp]="response.situationOn" />
                </div>
              </div>
              <div class="data2">
                <div class="title">
                  <span i18n="@@route.last-updated">Last updated</span>
                </div>
                <div class="body">
                  <kpn-timestamp [timestamp]="page.route.lastUpdated" />
                </div>
              </div>
              <kpn-data title="Relation last updated" i18n-title="@@route.relation-last-updated">
                <kpn-timestamp [timestamp]="page.route.summary.timestamp" />
              </kpn-data>
              <kpn-data title="Network" i18n-title="@@route.network">
                <kpn-route-network-references [references]="page.networkReferences" />
              </kpn-data>
              @if (page.route.analysis; as analysis) {
                <div>
                  @if (hasFreeNodes(analysis)) {
                    <kpn-data title="Nodes" i18n-title="@@route.nodes">
                      <kpn-route-free-nodes [analysis]="analysis" />
                    </kpn-data>
                  } @else {
                    <kpn-data title="Start node" i18n-title="@@route.start-node">
                      <kpn-route-start-nodes [analysis]="analysis" />
                    </kpn-data>
                    <kpn-data title="End node" i18n-title="@@route.end-node">
                      <kpn-route-end-nodes [analysis]="analysis" />
                    </kpn-data>
                  }
                  @if (analysis.map.redundantNodes.length > 0) {
                    <div>
                      <kpn-data title="Redundant node" i18n-title="@@route.redundant-node">
                        <kpn-route-redundant-nodes [analysis]="analysis" />
                      </kpn-data>
                    </div>
                  }
                  <kpn-data title="Number of ways" i18n-title="@@route.number-of-ways">
                    {{ page.route.summary.wayCount }}
                  </kpn-data>
                </div>
              }
              <kpn-data title="Tags" i18n-title="@@route.tags">
                <kpn-tags-table [tags]="routeTags(page)" />
              </kpn-data>
              <kpn-data title="Location" i18n-title="@@route.location">
                <kpn-route-location
                  [networkType]="networkType()"
                  [locationAnalysis]="page.route.analysis.locationAnalysis"
                />
              </kpn-data>
              @if (page.route.analysis && showRouteDetails$ | async) {
                <div>
                  <kpn-data title="Structure" i18n-title="@@route.structure">
                    <kpn-route-structure
                      [structureStrings]="page.route.analysis.structureStrings"
                    />
                  </kpn-data>
                </div>
              }
              <kpn-data title="Facts" i18n-title="@@route.facts">
                <kpn-facts [factInfos]="factInfos(page)" />
              </kpn-data>
              @if (showRouteDetails$ | async) {
                <div>
                  <kpn-route-members
                    [networkType]="page.route.summary.networkType"
                    [members]="page.route.analysis.members"
                  />
                </div>
              }
            </div>
          }
        </div>
      }
      <kpn-analysis-sidebar sidebar />
    </kpn-page>
  `,
  styleUrl: '../../../shared/components/shared/data/data.component.scss',
  standalone: true,
  imports: [
    AnalysisSidebarComponent,
    AsyncPipe,
    DataComponent,
    FactsComponent,
    PageComponent,
    RouteEndNodesComponent,
    RouteFreeNodesComponent,
    RouteLocationComponent,
    RouteMembersComponent,
    RouteNetworkReferencesComponent,
    RoutePageHeaderComponent,
    RouteRedundantNodesComponent,
    RouteStartNodesComponent,
    RouteStructureComponent,
    RouteSummaryComponent,
    RouterLink,
    TagsTableComponent,
    TimestampComponent,
    SymbolComponent,
  ],
})
export class RoutePageComponent implements OnInit, OnDestroy {
  private readonly pageWidthService = inject(PageWidthService);
  private readonly store = inject(Store);

  protected readonly routeId = this.store.selectSignal(selectRouteId);
  protected readonly routeName = this.store.selectSignal(selectRouteName);
  protected readonly changeCount = this.store.selectSignal(selectRouteChangeCount);
  protected readonly apiResponse = this.store.selectSignal(selectRouteDetailsPage);
  protected readonly networkType = this.store.selectSignal(selectRouteNetworkType);

  showRouteDetails$: Observable<boolean>;

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

  ngOnDestroy(): void {
    this.store.dispatch(actionRouteDetailsPageDestroy());
  }

  routeTags(page: RouteDetailsPage) {
    return InterpretedTags.routeTags(page.route.tags);
  }

  factInfos(page: RouteDetailsPage): FactInfo[] {
    return page.route.facts.map((fact) => {
      if (fact === 'RouteUnexpectedNode') {
        const unexpectedNodeIds = page.route.analysis.unexpectedNodeIds;
        return new FactInfo(fact, undefined, undefined, undefined, unexpectedNodeIds);
      }
      if (fact === 'RouteUnexpectedRelation') {
        const unexpectedRelationIds = page.route.analysis.unexpectedRelationIds;
        return new FactInfo(
          fact,
          undefined,
          undefined,
          undefined,
          undefined,
          unexpectedRelationIds
        );
      }
      return new FactInfo(fact);
    });
  }

  hasFreeNodes(analysis: RouteInfoAnalysis): boolean {
    return analysis.map.freeNodes && analysis.map.freeNodes.length > 0;
  }

  hasSymbol(page: RouteDetailsPage): boolean {
    return !!this.symbolDescription(page);
  }

  symbolDescription(page: RouteDetailsPage): string {
    const symbolTag = page.route.tags.tags.find((tag) => tag.key === 'osmc:symbol');
    if (symbolTag) {
      return symbolTag.value;
    }
    return undefined;
  }
}
