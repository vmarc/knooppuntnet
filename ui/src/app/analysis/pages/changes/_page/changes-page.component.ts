import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {ChangesPage} from "../../../../kpn/shared/changes-page";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'kpn-changes-page',
  templateUrl: './changes-page.component.html',
  styleUrls: ['./changes-page.component.scss']
})
export class ChangesPageComponent implements OnInit {

  response: ApiResponse<ChangesPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    this.appService.changes().subscribe(response => {
      this.response = response;
    });
  }

}
