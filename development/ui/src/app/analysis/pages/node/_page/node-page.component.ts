import {Component, OnDestroy, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {NodePage} from "../../../../kpn/shared/node/node-page";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";

@Component({
  selector: 'kpn-node-page',
  templateUrl: './node-page.component.html',
  styleUrls: ['./node-page.component.scss']
})
export class NodePageComponent implements OnInit, OnDestroy {

  response: ApiResponse<NodePage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      const nodeId = params['nodeId'];
      this.appService.node(nodeId).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

  get nodeInfo() {
    return this.response.result.nodeInfo;
  }

  get references() {
    return this.response.result.references;
  }

}
