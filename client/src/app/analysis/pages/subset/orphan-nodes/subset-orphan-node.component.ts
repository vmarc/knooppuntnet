import {Component, Input, OnInit} from "@angular/core";
import {NodeInfo} from "../../../../kpn/shared/node-info";
import {InterpretedTags} from "../../../../components/shared/tags/interpreted-tags";
import {Tags} from "../../../../kpn/shared/data/tags";

@Component({
  selector: "kpn-subset-orphan-node",
  template: `
    <p>
      <kpn-link-node [nodeId]="node.id" [nodeName]="node.name"></kpn-link-node>
    </p>
    <p>
      <kpn-timestamp [timestamp]="node.lastUpdated"></kpn-timestamp>
    </p>
    <p *ngIf="!extraTags.isEmpty()">
      <span>Extra tags:</span>
      <kpn-tags-table [tags]="extraTags"></kpn-tags-table>
    </p>
    <p>
      <osm-link-node nodeId="{{node.id}}"></osm-link-node>
      <josm-node nodeId="{{node.id}}"></josm-node>
    </p>
  `
})
export class SubsetOrphanNodeComponent implements OnInit {

  @Input() node: NodeInfo;
  extraTags: InterpretedTags;

  ngOnInit(): void {
    this.extraTags = InterpretedTags.all(new Tags(InterpretedTags.nodeTags(this.node.tags).extraTags()));
  }

}
