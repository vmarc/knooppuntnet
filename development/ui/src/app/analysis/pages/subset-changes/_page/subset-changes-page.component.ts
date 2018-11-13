import {Component, OnDestroy, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetChangesPage} from "../../../../kpn/shared/subset/subset-changes-page";
import {ActivatedRoute} from "@angular/router";
import {Subset} from "../../../../kpn/shared/subset";
import {Util} from "../../../../shared/util";
import {Subscription} from "rxjs";

@Component({
  selector: 'kpn-subset-changes-page',
  templateUrl: './subset-changes-page.component.html',
  styleUrls: ['./subset-changes-page.component.scss']
})
export class SubsetChangesPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetChangesPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
      this.response = null;
      this.appService.subsetChanges(this.subset).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

}
