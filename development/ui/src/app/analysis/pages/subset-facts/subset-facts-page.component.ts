import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {SubsetFactsPage} from "../../../kpn/shared/subset/subset-facts-page";
import {Util} from "../../../shared/util";
import {Subset} from "../../../kpn/shared/subset";

@Component({
  selector: 'kpn-subset-facts-page',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <kpn-subset-sidenav sidenav [subset]="subset"></kpn-subset-sidenav>
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
            <kpn-timestamp>{{response.situationOn}}</kpn-timestamp>
          </p>

          <div *ngIf="!hasFacts()">
            <i>No facts</i>
          </div>
          <div *ngIf="hasFacts()">
            <kpn-items>
              <kpn-item *ngFor="let factName of allFactNames; let i=index" index="{{i}}">
                <kpn-fact [factName]="factName"></kpn-fact>
              </kpn-item>
            </kpn-items>
          </div>
          <json [object]="response"></json>
        </div>
      </div>
    </kpn-page>
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
              private appService: AppService) {
  }

  ngOnInit() {
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
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
    return this.response && this.response.result && this.response.result.factCounts.length > 0;
  }

}
