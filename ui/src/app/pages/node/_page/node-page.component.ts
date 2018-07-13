import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../app.service";

@Component({
  selector: 'kpn-node-page',
  templateUrl: './node-page.component.html',
  styleUrls: ['./node-page.component.css']
})
export class NodePageComponent implements OnInit {

  content = "Loading...";

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.node(278003073).subscribe(content => {
      this.content = content;
    });
  }

}
