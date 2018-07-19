import { Component, OnInit } from '@angular/core';
import {AppService} from "../../../../app.service";

@Component({
  selector: 'kpn-subset-orphan-routes-page',
  templateUrl: './subset-orphan-routes-page.component.html',
  styleUrls: ['./subset-orphan-routes-page.component.scss']
})
export class SubsetOrphanRoutesPageComponent implements OnInit {

  content = "Loading...";

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.subsetOrphanRoutes().subscribe(content => {
      this.content = content;
    });
  }

}
