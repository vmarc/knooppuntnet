import {Component, OnDestroy, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetOrphanNodesPage} from "../../../../kpn/shared/subset/subset-orphan-nodes-page";
import {ActivatedRoute} from "@angular/router";
import {Util} from "../../../../shared/util";
import {Subset} from "../../../../kpn/shared/subset";
import {Subscription} from "rxjs";

@Component({
  selector: 'kpn-subset-orphan-nodes-page',
  templateUrl: './subset-orphan-nodes-page.component.html',
  styleUrls: ['./subset-orphan-nodes-page.component.scss']
})
export class SubsetOrphanNodesPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetOrphanNodesPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
      this.response = null;
      this.appService.subsetOrphanNodes(this.subset).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

}
