import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetFactsPage} from "../../../../kpn/shared/subset/subset-facts-page";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'kpn-subset-facts-page',
  templateUrl: './subset-facts-page.component.html',
  styleUrls: ['./subset-facts-page.component.scss']
})
export class SubsetFactsPageComponent implements OnInit {

  response: ApiResponse<SubsetFactsPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    const country = this.activatedRoute.snapshot.paramMap.get('country');
    const networkType = this.activatedRoute.snapshot.paramMap.get('networkType');
    this.appService.subsetFacts(country, networkType).subscribe(response => {
      this.response = response;
    });
  }

}
