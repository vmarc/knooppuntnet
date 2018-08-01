import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetOrphanNodesPage} from "../../../../kpn/shared/subset/subset-orphan-nodes-page";

@Component({
  selector: 'kpn-subset-orphan-nodes-page',
  templateUrl: './subset-orphan-nodes-page.component.html',
  styleUrls: ['./subset-orphan-nodes-page.component.scss']
})
export class SubsetOrphanNodesPageComponent implements OnInit {

  response: ApiResponse<SubsetOrphanNodesPage>;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.subsetOrphanNodes().subscribe(response => {
      this.response = response;
    });
  }

}
