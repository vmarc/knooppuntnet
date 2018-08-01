import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {NodePage} from "../../../../kpn/shared/node/node-page";
import {ApiResponse} from "../../../../kpn/shared/api-response";

@Component({
  selector: 'kpn-node-page',
  templateUrl: './node-page.component.html',
  styleUrls: ['./node-page.component.scss']
})
export class NodePageComponent implements OnInit {

  content = "Loading...";
  response: ApiResponse<NodePage>;

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.node(1 /*278003073*/).subscribe(response => {
      this.response = response;
    });
  }

  get nodeInfo() {
    return this.response.result.nodeInfo;
  }

  get references() {
    return this.response.result.references;
  }

}
