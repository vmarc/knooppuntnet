import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input, OnInit} from '@angular/core';
import {NodeInfo} from '../../../kpn/api/common/node-info';
import {InterpretedTags} from '../../../components/shared/tags/interpreted-tags';
import {Tags} from '../../../kpn/api/custom/tags';

@Component({
  selector: 'kpn-subset-orphan-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p>
      <kpn-link-node [nodeId]="node.id" [nodeName]="node.name"></kpn-link-node>
    </p>
    <p>
      <kpn-timestamp [timestamp]="node.lastUpdated"></kpn-timestamp>
    </p>
    <p *ngIf="!extraTags.isEmpty()">
      <span i18n="@@subset-orphan-nodes.extra-tags">Extra tags:</span>
      <kpn-tags-table [tags]="extraTags"></kpn-tags-table>
    </p>
    <p>
      <kpn-osm-link-node [nodeId]="node.id"></kpn-osm-link-node>
      <kpn-josm-node [nodeId]="node.id"></kpn-josm-node>
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
