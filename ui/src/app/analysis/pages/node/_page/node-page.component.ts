import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {Timestamp} from "../../../../kpn/shared/timestamp";
import {NodePage} from "../../../../kpn/shared/node/node-page";

@Component({
  selector: 'kpn-node-page',
  templateUrl: './node-page.component.html',
  styleUrls: ['./node-page.component.scss']
})
export class NodePageComponent implements OnInit {

  content = "Loading...";
  situationOn: Timestamp;
  nodePage: NodePage;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.node(1 /*278003073*/).subscribe(response => {
      this.content = JSON.stringify(response, null, 2);
      this.situationOn = response.situationOn;
      this.nodePage = response.result;
    });
  }

}
