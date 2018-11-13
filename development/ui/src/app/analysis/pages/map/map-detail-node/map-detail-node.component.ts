import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NodePage} from "../../../../kpn/shared/node/node-page";
import {AppService} from "../../../../app.service";
import {NetworkType} from "../../../../kpn/shared/network-type";

@Component({
  selector: 'kpn-map-detail-node',
  templateUrl: './map-detail-node.component.html',
  styleUrls: ['./map-detail-node.component.scss']
})
export class MapDetailNodeComponent implements OnChanges {

  @Input() nodeId: number;
  @Input() nodeName: string;
  @Input() networkType: NetworkType;

  response: ApiResponse<NodePage>;

  constructor(private appService: AppService) {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes["nodeId"]) {
      this.response = null;
      this.appService.node("" + this.nodeId).subscribe(response => {
        this.response = response;
      });
    }
  }

  get nodeInfo() {
    if (this.response && this.response.result) {
      return this.response.result.nodeInfo;
    }
    return null;
  }

  get references() {
    if (this.response && this.response.result) {
      return this.response.result.references;
    }
    return null;
  }

  get routeReferences() {
    if (this.response && this.response.result) {
      return this.response.result.references;
    }
    return null;
  }

}
