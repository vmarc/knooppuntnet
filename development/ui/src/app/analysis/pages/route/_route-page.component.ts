import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../app.service";
import {RoutePage} from "../../../kpn/shared/route/route-page";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {PageService} from "../../../components/shared/page.service";

@Component({
  selector: 'kpn-route-page',
  template: `
    <div *ngIf="response?.result">

      <div *ngIf="!response.result">
        <h1>Route not found</h1>
      </div>

      <div *ngIf="response.result">

        <h1>Route {{route.summary.name}}</h1>

        <kpn-data title="Summary"> <!-- "Samenvatting" -->
          <kpn-route-summary [route]="route"></kpn-route-summary>
        </kpn-data>

        <kpn-data title="Situation on"> <!-- "Situatie op" -->
          <kpn-timestamp [timestamp]="response.situationOn"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Last updated"> <!-- "Laatst bewerkt" -->
          <kpn-timestamp [timestamp]="route.lastUpdated"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Relation last updated"> <!-- "Relatie bewerkt" -->
          <kpn-timestamp [timestamp]="route.summary.timestamp"></kpn-timestamp>
        </kpn-data>

        <div *ngIf="route.ignored === false">

          <kpn-data title="Network"> <!-- "Netwerk" -->
            TODO UiNetworkReferences(page.references.networkReferences)
          </kpn-data>
         
          <div *ngIf="route.analysis">

            <kpn-data title="Start node"> <!-- "Start knooppunt" -->
              <p *ngIf="route.analysis.startNodes.isEmpty()">?</p>
              <p *ngFor="let node of route.analysis.startNodes">
                <kpn-route-node [node]="node" title="marker-icon-green-small.png"></kpn-route-node>
              </p>
              <p *ngFor="let node of route.analysis.startTentacleNodes">
                <kpn-route-node [node]="node" title="marker-icon-orange-small.png"></kpn-route-node>
              </p>
            </kpn-data>

            <kpn-data title="End node"> <!-- Eind knooppunt -->
              <p *ngIf="route.analysis.endNodes.isEmpty()">?</p>
              <p *ngFor="let node of route.analysis.endNodes">
                <kpn-route-node [node]="node" title="marker-icon-red-small.png"></kpn-route-node>
              </p>
              <p *ngFor="let node of route.analysis.endTentacleNodes">
                <kpn-route-node [node]="node" title="marker-icon-purple-small.png"></kpn-route-node>
              </p>
            </kpn-data>


            <div *ngIf="route.analysis.map.redundantNodes.isEmpty()">
              <kpn-data title="Redundant nodes"> <!-- Bijkomende knooppunten -->
                <p *ngFor="let node of route.analysis.map.redundantNodes">
                  <kpn-route-node [node]="node" title="marker-icon-yellow-small.png"></kpn-route-node>
                </p>
              </kpn-data>
            </div>

            <kpn-data title="Number of ways"> <!-- Aantal wegen -->
              <p>
                {{route.summary.wayCount}}
              </p>
            </kpn-data>
          </div>

          <kpn-data title="Tags"> <!-- Labels -->
            <p>
              TODO UiTagsTable(RouteTagFilter(page.route))
            </p>
          </kpn-data>


          <div *ngIf="route.analysis && true"> <!-- && (PageWidth.isLarge || PageWidth.isVeryLarge)) -->
            <kpn-data title="Structure"> <!-- Structuur -->
              <kpn-route-structure [structureStrings]="route.analysis.structureStrings"></kpn-route-structure>
            </kpn-data>
          </div>


          <kpn-data title="Facts"> <!-- Feiten -->
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

  response: ApiResponse<RoutePage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      const routeId = params['routeId'];
      this.appService.route(routeId).subscribe(response => {
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
