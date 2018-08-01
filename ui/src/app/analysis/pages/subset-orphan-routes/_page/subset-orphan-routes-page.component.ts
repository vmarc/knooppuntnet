import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetOrphanRoutesPage} from "../../../../kpn/shared/subset/subset-orphan-routes-page";

@Component({
  selector: 'kpn-subset-orphan-routes-page',
  templateUrl: './subset-orphan-routes-page.component.html',
  styleUrls: ['./subset-orphan-routes-page.component.scss']
})
export class SubsetOrphanRoutesPageComponent implements OnInit {

  response: ApiResponse<SubsetOrphanRoutesPage>;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.subsetOrphanRoutes().subscribe(response => {
      this.response = response;
    });
  }

}
