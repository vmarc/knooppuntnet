import { Component, OnInit } from '@angular/core';
import {AppService} from "../../../app.service";

@Component({
  selector: 'kpn-subset-networks-page',
  templateUrl: './subset-networks-page.component.html',
  styleUrls: ['./subset-networks-page.component.scss']
})
export class SubsetNetworksPageComponent implements OnInit {

  content = "Loading...";

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.subsetNetworks().subscribe(content => {
      this.content = content;
    });
  }

}
