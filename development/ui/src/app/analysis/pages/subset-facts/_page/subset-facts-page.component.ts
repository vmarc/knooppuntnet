import {Component, OnDestroy, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetFactsPage} from "../../../../kpn/shared/subset/subset-facts-page";
import {ActivatedRoute} from "@angular/router";
import {Util} from "../../../../shared/util";
import {Subset} from "../../../../kpn/shared/subset";
import {Subscription} from "rxjs";

@Component({
  selector: 'kpn-subset-facts-page',
  templateUrl: './subset-facts-page.component.html',
  styleUrls: ['./subset-facts-page.component.scss']
})
export class SubsetFactsPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetFactsPage>;
  paramsSubscription: Subscription;

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

}
