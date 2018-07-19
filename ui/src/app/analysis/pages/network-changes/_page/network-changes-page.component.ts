import { Component, OnInit } from '@angular/core';
import {AppService} from "../../../../app.service";

@Component({
  selector: 'kpn-network-changes-page',
  templateUrl: './network-changes-page.component.html',
  styleUrls: ['./network-changes-page.component.scss']
})
export class NetworkChangesPageComponent implements OnInit {

  content = "Loading...";

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.networkChanges(3138543).subscribe(content => {
      this.content = content;
    });
  }

}
