import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {List} from "immutable";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {PageWidthService} from "../../../components/shared/page-width.service";
import {PageService} from "../../../components/shared/page.service";
import {InterpretedTags} from "../../../components/shared/tags/interpreted-tags";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {RouteDetailsPage} from "../../../kpn/shared/route/route-details-page";
import {Subscriptions} from "../../../util/Subscriptions";
import {FactInfo} from "../../fact/fact-info";

@Component({
  selector: "kpn-route-page",
  template: `

    <kpn-route-page-header
      [routeId]="routeId"
      [routeName]="response?.result?.route.summary.name"
      [changeCount]="response?.result?.changeCount">
    </kpn-route-page-header>

    <div *ngIf="response">

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

        <div *ngIf="!route.ignored">

          <kpn-data title="Network" i18n-title="@@route.network">
            <kpn-route-network-references [references]="response.result.references.networkReferences"></kpn-route-network-references>
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


          <div *ngIf="route.analysis && !isPageSmall()">
            <kpn-data title="Structure" i18n-title="@@route.structure">
              <kpn-route-structure [structureStrings]="route.analysis.structureStrings"></kpn-route-structure>
            </kpn-data>
          </div>


          <kpn-data title="Facts" i18n-title="@@route.facts">
            <kpn-facts [factInfos]="factInfos()"></kpn-facts>
          </kpn-data>

          <div *ngIf="!isPageSmall()">
            <kpn-route-members [networkType]="route.summary.networkType" [members]="route.analysis.members"></kpn-route-members>
          </div>

        </div>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class RoutePageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  routeId: string;
  response: ApiResponse<RouteDetailsPage>;
  tags: InterpretedTags;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private pageWidthService: PageWidthService) {
  }

  ngOnInit(): void {
    this.pageService.defaultMenu();
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => params["routeId"]),
        tap(routeId => this.routeId = routeId),
        flatMap(routeId => this.appService.routeDetails(routeId))
      ).subscribe(response => this.processResponse(response))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  get route() {
    return this.response.result.route;
  }

  factInfos(): List<FactInfo> {
    return this.route.facts.map(fact => new FactInfo(fact));
  }

  isPageSmall(): boolean {
    return this.pageWidthService.isSmall();
  }

  private processResponse(response: ApiResponse<RouteDetailsPage>) {
    this.response = response;
    this.tags = InterpretedTags.routeTags(this.response.result.route.tags);
  }

}
