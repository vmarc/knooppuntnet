import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute, Params} from "@angular/router";
import {flatMap, map} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {Subset} from "../../../kpn/api/custom/subset";
import {SubsetFactDetailsPage} from "../../../kpn/api/common/subset/subset-fact-details-page";
import {SubsetCacheService} from "../../../services/subset-cache.service";
import {Subscriptions} from "../../../util/Subscriptions";

class SubsetFact {
  constructor(readonly subset: Subset,
              readonly factName: string) {
  }
}

@Component({
  selector: "kpn-subset-fact-details-page",
  template: `

    <kpn-subset-page-header-block
      [subset]="subset"
      pageName="facts"
      pageTitle="Facts"
      i18n-pageTitle="@@subset-facts.title">
    </kpn-subset-page-header-block>

    <h2>
      <kpn-fact-name [factName]="factName"></kpn-fact-name>
    </h2>

    <kpn-fact-description [factName]="factName"></kpn-fact-description>

    <div *ngIf="response">
      <p>
        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
      </p>
      <div *ngIf="!hasFacts()">
        <i i18n="@@subset-facts.no-facts">No facts</i>
      </div>
      <div *ngIf="hasFacts()">
        <p>
          {{refCount()}}
          <span *ngIf="hasNodeRefs()" i18n="@@subset-facts.node-refs">nodes</span>
          <span *ngIf="hasRouteRefs()" i18n="@@subset-facts.route-refs">routes</span>
          <span *ngIf="hasOsmNodeRefs()" i18n="@@subset-facts.osm-node-refs">nodes</span>
          <span *ngIf="hasOsmWayRefs()" i18n="@@subset-facts.osm-way-refs">ways</span>
          <span *ngIf="hasOsmRelationRefs()" i18n="@@subset-facts.osm-relation-refs">relations</span>
          <span i18n="@@subset-facts.in-networks">in {{response.result.networks.size}} networks.</span>
        </p>

        <kpn-items>
          <kpn-item *ngFor="let networkFactRefs of response.result.networks; let i=index" [index]="i">
            <a [routerLink]="'/analysis/network/' + networkFactRefs.networkId">
              {{networkFactRefs.networkName}}
            </a>
            <br/>
            <span i18n="@@subset-facts.routes">{{networkFactRefs.factRefs.size}} routes:</span>
            <br/>
            <div class="kpn-comma-list">
              <span *ngFor="let ref of networkFactRefs.factRefs">
                <a *ngIf="hasNodeRefs()" [routerLink]="'/analysis/node/' + ref.id">{{ref.name}}</a>
                <a *ngIf="hasRouteRefs()" [routerLink]="'/analysis/route/' + ref.id">{{ref.name}}</a>
                <kpn-osm-link-node *ngIf="hasOsmNodeRefs()" id="ref.id" title="ref.id"></kpn-osm-link-node>
                <kpn-osm-link-way *ngIf="hasOsmWayRefs()" id="ref.id" title="ref.id"></kpn-osm-link-way>
                <kpn-osm-link-relation *ngIf="hasOsmRelationRefs()" id="ref.id" title="ref.id"></kpn-osm-link-relation>
              </span>
            </div>
          </kpn-item>
        </kpn-items>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class SubsetFactDetailsPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  factName: String;
  response: ApiResponse<SubsetFactDetailsPage>;
  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private subsetCacheService: SubsetCacheService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => this.interpreteParams(params)),
        flatMap(subsetFact => this.appService.subsetFactDetails(subsetFact.subset /* TODO: add parameter, subsetFact.factName*/))
      ).subscribe(response => this.processResponse(response))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  hasFacts() {
    return this.response && this.response.result && this.response.result.networks.size > 0;
  }

  refCount(): number {
    return this.response.result.networks.map(n => n.factRefs.size).reduce((sum, current) => sum + current);
  }

  hasNodeRefs(): boolean {
    return this.factName === "NodeMemberMissing"
      || this.factName === "IntegrityCheckFailed";
  }

  hasOsmNodeRefs(): boolean {
    return this.factName === "NetworkExtraMemberNode";
  }

  hasOsmWayRefs(): boolean {
    return this.factName === "NetworkExtraMemberWay";
  }

  hasOsmRelationRefs(): boolean {
    return this.factName === "NetworkExtraMemberRelation";
  }

  hasRouteRefs(): boolean {
    return !(this.hasNodeRefs()
      || this.hasOsmNodeRefs()
      || this.hasOsmWayRefs()
      || this.hasOsmRelationRefs());
  }

  private interpreteParams(params: Params): SubsetFact {
    const subset = Util.subsetInRoute(params);
    const factName = params["fact"];
    this.subset = subset;
    this.factName = factName;
    return new SubsetFact(subset, factName);
  }

  private processResponse(response: ApiResponse<SubsetFactDetailsPage>) {
    this.response = response;
    this.subsetCacheService.setSubsetInfo(this.subset.key(), this.response.result.subsetInfo);
  }

}
