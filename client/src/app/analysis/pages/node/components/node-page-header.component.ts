import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-node-page-header",
  template: `

    <kpn-page-header [pageTitle]="nodeName" subject="node-page">
      <span>Node</span>
      <span *ngIf="nodeName">&nbsp;{{nodeName}}</span>
    </kpn-page-header>

    <kpn-page-menu>

      <kpn-page-menu-option
        [link]="linkNodeDetails()"
        pageTitle="Details">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkNodeMap()"
        pageTitle="Map">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkNodeChanges()"
        pageTitle="Changes">
      </kpn-page-menu-option>

    </kpn-page-menu>

  `
})
export class NodePageHeaderComponent {

  @Input() nodeId: number;
  @Input() nodeName: string;

  linkNodeDetails(): string {
    return this.linkRoute("");
  }

  linkNodeMap(): string {
    return this.linkRoute("/map");
  }

  linkNodeChanges(): string {
    return this.linkRoute("/changes");
  }

  private linkRoute(suffix: string): string {
    return `/analysis/node/${this.nodeId}${suffix}`;
  }

}
