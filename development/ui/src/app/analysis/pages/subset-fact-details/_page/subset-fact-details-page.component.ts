import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetFactDetailsPage} from "../../../../kpn/shared/subset/subset-fact-details-page";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'kpn-subset-fact-details-page',
  templateUrl: './subset-fact-details-page.component.html',
  styleUrls: ['./subset-fact-details-page.component.scss']
})
export class SubsetFactDetailsPageComponent implements OnInit {

  response: ApiResponse<SubsetFactDetailsPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    const country = this.activatedRoute.snapshot.paramMap.get('country');
    const networkType = this.activatedRoute.snapshot.paramMap.get('networkType');
    this.appService.subsetFactDetails(country, networkType).subscribe(response => {
      this.response = response;
    });
  }

}
