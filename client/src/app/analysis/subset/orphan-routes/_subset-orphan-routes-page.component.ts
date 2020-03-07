import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {List} from "immutable";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {RouteSummary} from "../../../kpn/api/common/route-summary";
import {Subset} from "../../../kpn/api/custom/subset";
import {SubsetOrphanRoutesPage} from "../../../kpn/api/common/subset/subset-orphan-routes-page";
import {SubsetCacheService} from "../../../services/subset-cache.service";
import {Subscriptions} from "../../../util/Subscriptions";

@Component({
  selector: "kpn-subset-orphan-routes-page",
  template: `

    <kpn-subset-page-header-block
      [subset]="subset"
      pageName="orphan-routes"
      pageTitle="Orphan routes"
      i18n-pageTitle="@@subset-orphan-routes.title">
    </kpn-subset-page-header-block>

    <div *ngIf="response">
      <p>
        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
      </p>
      <div *ngIf="routes.isEmpty()" class="kpn-line">
        <kpn-icon-happy></kpn-icon-happy>
        <span i18n="@@subset-orphan-routes.no-routes">No orphan routes</span>
      </div>
      <div *ngIf="!routes.isEmpty()">
        <kpn-subset-orphan-routes-table
          [timeInfo]="response.result.timeInfo"
          [orphanRoutes]="response.result.rows">
        </kpn-subset-orphan-routes-table>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class SubsetOrphanRoutesPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetOrphanRoutesPage>;
  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private subsetCacheService: SubsetCacheService) {
  }

  get routes(): List<RouteSummary> {
    return this.response.result.rows;
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => Util.subsetInRoute(params)),
        tap(subset => this.subset = subset),
        flatMap(subset => this.appService.subsetOrphanRoutes(subset))
      ).subscribe(response => this.processResponse(response))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  private processResponse(response: ApiResponse<SubsetOrphanRoutesPage>) {
    this.response = response;
    this.subsetCacheService.setSubsetInfo(this.subset.key(), this.response.result.subsetInfo);
  }

}
