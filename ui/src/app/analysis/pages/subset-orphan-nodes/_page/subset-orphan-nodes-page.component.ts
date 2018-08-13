import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetOrphanNodesPage} from "../../../../kpn/shared/subset/subset-orphan-nodes-page";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'kpn-subset-orphan-nodes-page',
  templateUrl: './subset-orphan-nodes-page.component.html',
  styleUrls: ['./subset-orphan-nodes-page.component.scss']
})
export class SubsetOrphanNodesPageComponent implements OnInit {

  response: ApiResponse<SubsetOrphanNodesPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    const country = this.activatedRoute.snapshot.paramMap.get('country');
    const networkType = this.activatedRoute.snapshot.paramMap.get('networkType');
    this.appService.subsetOrphanNodes(country, networkType).subscribe(response => {
      this.response = response;
    });
  }

}
