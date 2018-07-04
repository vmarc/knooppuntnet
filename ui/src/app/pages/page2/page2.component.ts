import { Component, OnInit } from '@angular/core';
import {AppService} from "../../app.service";

@Component({
  selector: 'kpn-page2',
  templateUrl: './page2.component.html',
  styleUrls: ['./page2.component.css']
})
export class Page2Component implements OnInit {

  content: string;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.getPage2().subscribe((data: any) => {
      this.content = data.content;
    });
  }

}
