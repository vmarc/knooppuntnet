import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {List} from "immutable";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {NodeInfo} from "../../../kpn/shared/node-info";
import {Subset} from "../../../kpn/shared/subset";
import {SubsetOrphanNodesPage} from "../../../kpn/shared/subset/subset-orphan-nodes-page";
import {SubsetCacheService} from "../../../services/subset-cache.service";
import {Subscriptions} from "../../../util/Subscriptions";

@Component({
  selector: "kpn-subset-orphan-nodes-page",
  template: `

    <kpn-subset-page-header-block
      [subset]="subset"
      pageName="orphan-nodes"
      pageTitle="Orphan nodes"
      i18n-pageTitle="@@subset-orphan-nodes.title">
    </kpn-subset-page-header-block>

    <div *ngIf="response">
      <div *ngIf="nodes.isEmpty()" class="kpn-line">
        <mat-icon svgIcon="happy"></mat-icon>
        <span i18n="@@subset-orphan-nodes.no-routes">No orphan nodes</span>
      </div>
      <div *ngIf="!nodes.isEmpty()">
        <p>
          <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
        </p>
        <kpn-subset-orphan-nodes-table
          [timeInfo]="response.result.timeInfo"
          [nodes]="nodes">
        </kpn-subset-orphan-nodes-table>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class SubsetOrphanNodesPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetOrphanNodesPage>;
  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private subsetCacheService: SubsetCacheService) {
  }

  get nodes(): List<NodeInfo> {
    return this.response.result.rows;
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => Util.subsetInRoute(params)),
        tap(subset => this.subset = subset),
        flatMap(subset => this.appService.subsetOrphanNodes(subset))
      ).subscribe(response => this.processResponse(response))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  private processResponse(response: ApiResponse<SubsetOrphanNodesPage>) {
    this.response = response;
    this.subsetCacheService.setSubsetInfo(this.subset.key(), this.response.result.subsetInfo)
  }

}
