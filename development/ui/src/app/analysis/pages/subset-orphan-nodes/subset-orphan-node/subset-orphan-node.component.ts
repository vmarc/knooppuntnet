import {Component, Input, OnInit} from '@angular/core';
import {NodeInfo} from "../../../../kpn/shared/node-info";

@Component({
  selector: 'kpn-subset-orphan-node',
  templateUrl: './subset-orphan-node.component.html',
  styleUrls: ['./subset-orphan-node.component.scss']
})
export class SubsetOrphanNodeComponent {

  @Input() node: NodeInfo;

  extraTags() {
    // TODO Tags(RouteTagFilter(route).extraTags)
    return this.node.tags;
  }

}
