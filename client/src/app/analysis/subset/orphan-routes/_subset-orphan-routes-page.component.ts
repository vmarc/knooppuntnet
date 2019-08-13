import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {Subset} from "../../../kpn/shared/subset";
import {SubsetOrphanRoutesPage} from "../../../kpn/shared/subset/subset-orphan-routes-page";
import {SubsetCacheService} from "../../../services/subset-cache.service";
import {flatMap, map, tap} from "rxjs/operators";
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
      <kpn-subset-orphan-routes-table [orphanRoutes]="response.result.rows"></kpn-subset-orphan-routes-table>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class SubsetOrphanRoutesPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  subset: Subset;
  response: ApiResponse<SubsetOrphanRoutesPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private subsetCacheService: SubsetCacheService) {
  }

  ngOnInit() {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => Util.subsetInRoute(params)),
        tap(subset => this.processSubset(subset)),
        flatMap(subset => this.appService.subsetOrphanRoutes(subset))
      ).subscribe(response => this.processResponse(response))
    );
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  private processSubset(subset: Subset) {
    this.subset = subset;
    this.pageService.subset = subset;
  }

  private processResponse(response: ApiResponse<SubsetOrphanRoutesPage>) {
    this.response = response;
    this.subsetCacheService.setSubsetInfo(this.subset.key(), this.response.result.subsetInfo)
  }

}
