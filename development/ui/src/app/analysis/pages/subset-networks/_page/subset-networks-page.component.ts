import {Component, OnDestroy, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetNetworksPage} from "../../../../kpn/shared/subset/subset-networks-page";
import {ActivatedRoute} from "@angular/router";
import {Util} from "../../../../shared/util";
import {Subset} from "../../../../kpn/shared/subset";
import {Subscription} from "rxjs";

@Component({
  selector: 'kpn-subset-networks-page',
  templateUrl: './subset-networks-page.component.html',
  styleUrls: ['./subset-networks-page.component.scss']
})
export class SubsetNetworksPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetNetworksPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
      this.response = null;
      this.appService.subsetNetworks(this.subset).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

}
