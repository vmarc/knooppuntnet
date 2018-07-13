import { Component, OnInit } from '@angular/core';
import {AppService} from "../../../app.service";

@Component({
  selector: 'kpn-subset-changes-page',
  templateUrl: './subset-changes-page.component.html',
  styleUrls: ['./subset-changes-page.component.css']
})
export class SubsetChangesPageComponent implements OnInit {

  content = "Loading...";

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.subsetChanges().subscribe(content => {
      this.content = content;
    });
  }

}
