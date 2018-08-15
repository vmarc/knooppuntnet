import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetChangesPage} from "../../../../kpn/shared/subset/subset-changes-page";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'kpn-subset-changes-page',
  templateUrl: './subset-changes-page.component.html',
  styleUrls: ['./subset-changes-page.component.scss']
})
export class SubsetChangesPageComponent implements OnInit {

  response: ApiResponse<SubsetChangesPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    const country = this.activatedRoute.snapshot.paramMap.get('country');
    const networkType = this.activatedRoute.snapshot.paramMap.get('networkType');
    this.appService.subsetChanges(country, networkType).subscribe(response => {
      this.response = response;
    });
  }

}
