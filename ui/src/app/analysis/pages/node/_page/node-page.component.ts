import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {NodePage} from "../../../../kpn/shared/node/node-page";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'kpn-node-page',
  templateUrl: './node-page.component.html',
  styleUrls: ['./node-page.component.scss']
})
export class NodePageComponent implements OnInit {

  response: ApiResponse<NodePage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    const nodeId = this.activatedRoute.snapshot.paramMap.get('nodeId');
    this.appService.node(nodeId).subscribe(response => {
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
