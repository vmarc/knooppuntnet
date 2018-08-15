import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetOrphanRoutesPage} from "../../../../kpn/shared/subset/subset-orphan-routes-page";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'kpn-subset-orphan-routes-page',
  templateUrl: './subset-orphan-routes-page.component.html',
  styleUrls: ['./subset-orphan-routes-page.component.scss']
})
export class SubsetOrphanRoutesPageComponent implements OnInit {

  response: ApiResponse<SubsetOrphanRoutesPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    const country = this.activatedRoute.snapshot.paramMap.get('country');
    const networkType = this.activatedRoute.snapshot.paramMap.get('networkType');
    this.appService.subsetOrphanRoutes(country, networkType).subscribe(response => {
      this.response = response;
    });
  }

}
