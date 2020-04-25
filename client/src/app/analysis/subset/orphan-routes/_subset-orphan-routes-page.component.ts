import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {List} from "immutable";
import {Observable} from "rxjs";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {Util} from "../../../components/shared/util";
import {RouteSummary} from "../../../kpn/api/common/route-summary";
import {SubsetOrphanRoutesPage} from "../../../kpn/api/common/subset/subset-orphan-routes-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {Subset} from "../../../kpn/api/custom/subset";
import {SubsetCacheService} from "../../../services/subset-cache.service";

@Component({
  selector: "kpn-subset-orphan-routes-page",
  template: `

    <kpn-subset-page-header-block
      [subset]="subset$ | async"
      pageName="orphan-routes"
      pageTitle="Orphan routes"
      i18n-pageTitle="@@subset-orphan-routes.title">
    </kpn-subset-page-header-block>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <p>
        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
      </p>
      <p *ngIf="routes.isEmpty()" class="kpn-line">
        <kpn-icon-happy></kpn-icon-happy>
        <span i18n="@@subset-orphan-routes.no-routes">No orphan routes</span>
      </p>
      <div *ngIf="!routes.isEmpty()">
        <kpn-subset-orphan-routes-table
          [timeInfo]="response.result.timeInfo"
          [orphanRoutes]="response.result.rows">
        </kpn-subset-orphan-routes-table>
      </div>
    </div>
  `
})
export class SubsetOrphanRoutesPageComponent implements OnInit {

  subset$: Observable<Subset>;
  response$: Observable<ApiResponse<SubsetOrphanRoutesPage>>;

  routes: List<RouteSummary>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private subsetCacheService: SubsetCacheService) {
  }

  ngOnInit(): void {
    this.subset$ = this.activatedRoute.params.pipe(map(params => Util.subsetInRoute(params)));
    this.response$ = this.subset$.pipe(
      flatMap(subset => this.appService.subsetOrphanRoutes(subset).pipe(
        tap(response => {
          this.routes = response.result.rows;
          this.subsetCacheService.setSubsetInfo(subset.key(), response.result.subsetInfo);
        })
      ))
    );
  }

}
