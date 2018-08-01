import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {SubsetChangesPage} from "../../../../kpn/shared/subset/subset-changes-page";

@Component({
  selector: 'kpn-subset-changes-page',
  templateUrl: './subset-changes-page.component.html',
  styleUrls: ['./subset-changes-page.component.scss']
})
export class SubsetChangesPageComponent implements OnInit {

  response: ApiResponse<SubsetChangesPage>;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.subsetChanges().subscribe(response => {
      this.response = response;
    });
  }

}
