import {Component, OnInit} from '@angular/core';
import {AppService} from "../../app.service";

@Component({
  selector: 'kpn-page3',
  templateUrl: './page3.component.html',
  styleUrls: ['./page3.component.css']
})
export class Page3Component implements OnInit {

  content: string;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.getPage3().subscribe((data: any) => {
      this.content = data.content;
    });
  }

}
