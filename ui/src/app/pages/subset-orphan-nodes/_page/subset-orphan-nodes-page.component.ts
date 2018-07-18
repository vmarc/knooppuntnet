import { Component, OnInit } from '@angular/core';
import {AppService} from "../../../app.service";

@Component({
  selector: 'kpn-subset-orphan-nodes-page',
  templateUrl: './subset-orphan-nodes-page.component.html',
  styleUrls: ['./subset-orphan-nodes-page.component.scss']
})
export class SubsetOrphanNodesPageComponent implements OnInit {

  content = "Loading...";

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.subsetOrphanNodes().subscribe(content => {
      this.content = content;
    });
  }

}
