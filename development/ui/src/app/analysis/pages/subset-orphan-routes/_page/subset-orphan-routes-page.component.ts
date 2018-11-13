import {Component, OnDestroy, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetOrphanRoutesPage} from "../../../../kpn/shared/subset/subset-orphan-routes-page";
import {ActivatedRoute} from "@angular/router";
import {Subset} from "../../../../kpn/shared/subset";
import {Util} from "../../../../shared/util";
import {Subscription} from "rxjs";

@Component({
  selector: 'kpn-subset-orphan-routes-page',
  templateUrl: './subset-orphan-routes-page.component.html',
  styleUrls: ['./subset-orphan-routes-page.component.scss']
})
export class SubsetOrphanRoutesPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetOrphanRoutesPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
      this.response = null;
      this.appService.subsetOrphanRoutes(this.subset).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

}
