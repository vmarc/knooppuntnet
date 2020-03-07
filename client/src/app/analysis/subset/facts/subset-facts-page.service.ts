import {Injectable} from "@angular/core";
import {Params} from "@angular/router";
import {ReplaySubject} from "rxjs";
import {Observable} from "rxjs";
import {switchMap} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {Util} from "../../../components/shared/util";
import {SubsetFactsPage} from "../../../kpn/api/common/subset/subset-facts-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {Subset} from "../../../kpn/api/custom/subset";
import {SubsetCacheService} from "../../../services/subset-cache.service";

@Injectable()
export class SubsetFactsPageService {

  readonly subset: Observable<Subset>;
  readonly response: Observable<ApiResponse<SubsetFactsPage>>;

  private readonly _subset = new ReplaySubject<Subset>();

  constructor(private appService: AppService, private subsetCacheService: SubsetCacheService) {
    this.subset = this._subset.asObservable();
    this.response = this._subset.pipe(
      switchMap((subset) =>
        this.appService.subsetFacts(subset).pipe(
          tap(response => {
            this.subsetCacheService.setSubsetInfo(subset.key(), response.result.subsetInfo);
          })
        )
      )
    );
  }

  params(params: Params) {
    this._subset.next(Util.subsetInRoute(params));
  }

}
