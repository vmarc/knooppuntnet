import { Component, OnInit } from '@angular/core';
import {AppService} from "../../../../app.service";

@Component({
  selector: 'kpn-network-facts-page',
  templateUrl: './network-facts-page.component.html',
  styleUrls: ['./network-facts-page.component.scss']
})
export class NetworkFactsPageComponent implements OnInit {

  content = "Loading...";

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.networkFacts(3138543).subscribe(content => {
      this.content = content;
    });
  }

}
