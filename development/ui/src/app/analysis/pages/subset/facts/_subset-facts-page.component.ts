import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetFactsPage} from "../../../../kpn/shared/subset/subset-facts-page";
import {Util} from "../../../../components/shared/util";
import {Subset} from "../../../../kpn/shared/subset";
import {PageService} from "../../../../components/shared/page.service";

@Component({
  selector: 'kpn-subset-facts-page',
  template: `
    <div content>
      <h1>
        <kpn-subset-name [subset]="subset"></kpn-subset-name>
      </h1>
      <h2>
        Facts
      </h2>
      <div *ngIf="response">
        <p>
          Situation on:
          <kpn-timestamp [timestamp]="response.situationOn"></kpn-timestamp>
        </p>
        <div *ngIf="!hasFacts()">
          <i>No facts</i>
        </div>
        <div *ngIf="hasFacts()">
          <kpn-items>
            <kpn-item *ngFor="let factName of allFactNames; let i=index" index="{{i}}">
              <p>
                <kpn-fact-name [factName]="factName"></kpn-fact-name>
              </p>
              <p>
                <kpn-fact-description [factName]="factName"></kpn-fact-description>
              </p>
            </kpn-item>
          </kpn-items>
        </div>
        <json [object]="response"></json>
      </div>
    </div>
  `
})
export class SubsetFactsPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetFactsPage>;
  paramsSubscription: Subscription;

  allFactNames = [
    "RouteNotContinious",
    "RouteUnusedSegments",
    "RouteNodeMissingInWays",
    "RouteRedundantNodes",
    "RouteWithoutWays",
    "RouteFixmetodo",
    "RouteNameMissing",
    "RouteEndNodeMismatch",
    "RouteStartNodeMismatch",
    "RouteTagMissing",
    "RouteTagInvalid",
    "RouteUnexpectedNode",
    "RouteUnexpectedRelation",
    "NetworkExtraMemberNode",
    "NetworkExtraMemberWay",
    "NetworkExtraMemberRelation",
    "NodeMemberMissing",
    "IntegrityCheckFailed",
    "OrphanRoute",
    "OrphanNode",
    "RouteIncomplete",
    "RouteIncompleteOk",
    "RouteUnaccessible",
    "RouteInvalidSortingOrder",
    "RouteReversed",
    "RouteNodeNameMismatch",
    "RouteNotForward",
    "RouteNotBackward",
    "RouteAnalysisFailed",
    "RouteOverlappingWays",
    "RouteSuspiciousWays",
    "RouteBroken",
    "RouteOneWay",
    "RouteNotOneWay",
    "NameMissing",
    "IgnoreForeignCountry",
    "IgnoreNoNetworkNodes",
    "IgnoreUnsupportedSubset",
    "Added",
    "BecomeIgnored",
    "BecomeOrphan",
    "Deleted",
    "IgnoreNetworkCollection",
    "IntegrityCheck",
    "LostBicycleNodeTag",
    "LostHikingNodeTag",
    "LostRouteTags",
    "WasIgnored",
    "WasOrphan"
  ];

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.initSubsetPage();
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
      this.pageService.subset = this.subset;
      this.response = null;
      this.appService.subsetFacts(this.subset).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

  hasFacts() {
    return this.response && this.response.result && this.response.result.factCounts.size > 0;
  }

}
