import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetFactDetailsPage} from "../../../../kpn/shared/subset/subset-fact-details-page";

@Component({
  selector: 'kpn-subset-fact-details-page',
  templateUrl: './subset-fact-details-page.component.html',
  styleUrls: ['./subset-fact-details-page.component.scss']
})
export class SubsetFactDetailsPageComponent implements OnInit {

  response: ApiResponse<SubsetFactDetailsPage>;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.subsetFactDetails().subscribe(response => {
      this.response = response;
    });
  }

}
