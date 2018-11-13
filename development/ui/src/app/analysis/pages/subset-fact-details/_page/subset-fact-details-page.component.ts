import {Component, OnDestroy, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetFactDetailsPage} from "../../../../kpn/shared/subset/subset-fact-details-page";
import {ActivatedRoute} from "@angular/router";
import {Util} from "../../../../shared/util";
import {Subset} from "../../../../kpn/shared/subset";
import {Subscription} from "rxjs";

@Component({
  selector: 'kpn-subset-fact-details-page',
  templateUrl: './subset-fact-details-page.component.html',
  styleUrls: ['./subset-fact-details-page.component.scss']
})
export class SubsetFactDetailsPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetFactDetailsPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
      this.response = null;
      this.appService.subsetFactDetails(this.subset).subscribe(response => {
        this.response = response;
      });
    });
  }


  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }
}
