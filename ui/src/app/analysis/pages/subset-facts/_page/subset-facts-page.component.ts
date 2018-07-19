import { Component, OnInit } from '@angular/core';
import {AppService} from "../../../../app.service";

@Component({
  selector: 'kpn-subset-facts-page',
  templateUrl: './subset-facts-page.component.html',
  styleUrls: ['./subset-facts-page.component.scss']
})
export class SubsetFactsPageComponent implements OnInit {

  content = "Loading...";

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.subsetFacts().subscribe(content => {
      this.content = content;
    });
  }

}
