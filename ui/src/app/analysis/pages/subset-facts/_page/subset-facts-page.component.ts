import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetFactsPage} from "../../../../kpn/shared/subset/subset-facts-page";

@Component({
  selector: 'kpn-subset-facts-page',
  templateUrl: './subset-facts-page.component.html',
  styleUrls: ['./subset-facts-page.component.scss']
})
export class SubsetFactsPageComponent implements OnInit {

  response: ApiResponse<SubsetFactsPage>;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.subsetFacts().subscribe(response => {
      this.response = response;
    });
  }

}
