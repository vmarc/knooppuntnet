import { Component, OnInit } from '@angular/core';
import {AppService} from "../../app.service";

@Component({
  selector: 'kpn-page1',
  templateUrl: './page1.component.html',
  styleUrls: ['./page1.component.css']
})
export class Page1Component implements OnInit {

  content: string;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.getPage1().subscribe((data: any) => {
      this.content = data.content;
    });
  }

}
