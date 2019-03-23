import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../../app.service";
import {RoutePage} from "../../../../kpn/shared/route/route-page";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {PageService} from "../../../../components/shared/page.service";

@Component({
  selector: 'kpn-route-page',
  template: `
    
    <kpn-route-page-header [routeId]="routeId" [routeName]="response?.result?.route.summary.name" [pageName]="'route'"></kpn-route-page-header>

    <div *ngIf="response">

      <div *ngIf="!response.result">
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

        <div *ngIf="route.ignored === false">

          <kpn-data title="Network" i18n-title="@@route.network">
            TODO UiNetworkReferences(page.references.networkReferences)
          </kpn-data>
         
          <div *ngIf="route.analysis">

            <kpn-data title="Start node" i18n-title="@@route.start-node">
              <p *ngIf="route.analysis.startNodes.isEmpty()">?</p>
              <p *ngFor="let node of route.analysis.startNodes">
                <kpn-route-node [node]="node" title="marker-icon-green-small.png"></kpn-route-node>
              </p>
              <p *ngFor="let node of route.analysis.startTentacleNodes">
                <kpn-route-node [node]="node" title="marker-icon-orange-small.png"></kpn-route-node>
              </p>
            </kpn-data>

            <kpn-data title="End node" i18n-title="@@route.end-node">
              <p *ngIf="route.analysis.endNodes.isEmpty()">?</p>
              <p *ngFor="let node of route.analysis.endNodes">
                <kpn-route-node [node]="node" title="marker-icon-red-small.png"></kpn-route-node>
              </p>
              <p *ngFor="let node of route.analysis.endTentacleNodes">
                <kpn-route-node [node]="node" title="marker-icon-purple-small.png"></kpn-route-node>
              </p>
            </kpn-data>


            <div *ngIf="route.analysis.map.redundantNodes.isEmpty()">
              <kpn-data title="Redundant nodes" i18n-title="@@route.redundant-nodes">
                <p *ngFor="let node of route.analysis.map.redundantNodes">
                  <kpn-route-node [node]="node" title="marker-icon-yellow-small.png"></kpn-route-node>
                </p>
              </kpn-data>
            </div>

            <kpn-data title="Number of ways" i18n-title="@@route.number-of-ways">
              <p>
                {{route.summary.wayCount}}
              </p>
            </kpn-data>
          </div>

          <kpn-data title="Tags" i18n-title="@@route.tags">
            <p>
              TODO UiTagsTable(RouteTagFilter(page.route))
            </p>
          </kpn-data>


          <div *ngIf="route.analysis && true"> <!-- && (PageWidth.isLarge || PageWidth.isVeryLarge)) -->
            <kpn-data title="Structure" i18n-title="@@route.structure">
              <kpn-route-structure [structureStrings]="route.analysis.structureStrings"></kpn-route-structure>
            </kpn-data>
          </div>


          <kpn-data title="Facts" i18n-title="@@route.facts">
            TODO UiFacts(route.facts.map(FactInfo(_)))
          </kpn-data>

          <div *ngIf="true"> <!-- (PageWidth.isLarge || PageWidth.isVeryLarge)) -->
            <kpn-route-members [networkType]="route.summary.networkType" [members]="route.analysis.members"></kpn-route-members>
          </div>

          <!--
            TagMod.when(page.routeChangeInfos.changes.nonEmpty) {
              UiRouteChanges(page.routeChangeInfos)
            }
          -->

          <!--
            TagMod.when(route.analysis.isDefined && PageWidth.isVeryLarge) {
              UiEmbeddedMap(new MainRouteMap(route.summary.networkType, route.analysis.get.map))
            }
          -->

        </div>
      </div>
      <json [object]="response"></json>
    </div>
  `
})
export class RoutePageComponent implements OnInit, OnDestroy {

  routeId: string;
  response: ApiResponse<RoutePage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.routeId = params['routeId'];
      this.appService.route(this.routeId).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

  get route() {
    return this.response.result.route;
  }
}
