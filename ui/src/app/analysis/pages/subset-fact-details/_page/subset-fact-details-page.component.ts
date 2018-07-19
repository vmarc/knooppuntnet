import { Component, OnInit } from '@angular/core';
import {AppService} from "../../../../app.service";

@Component({
  selector: 'kpn-subset-fact-details-page',
  templateUrl: './subset-fact-details-page.component.html',
  styleUrls: ['./subset-fact-details-page.component.scss']
})
export class SubsetFactDetailsPageComponent implements OnInit {

  content = "Loading...";

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.subsetFactDetails().subscribe(content => {
      this.content = content;
    });
  }

}
