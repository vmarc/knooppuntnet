import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-node-page-header",
  template: `

    <kpn-page-header [pageTitle]="nodeName" subject="node-page">
      <span i18n="@@node.title">Node</span>
      <span *ngIf="nodeName">&nbsp;{{nodeName}}</span>
    </kpn-page-header>

    <kpn-page-menu>

      <kpn-page-menu-option
        [link]="linkNodeDetails()"
        pageTitle="Details"
        i18n-pageTitle="@@node.menu.details">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkNodeMap()"
        pageTitle="Map"
        i18n-pageTitle="@@node.menu.map">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkNodeChanges()"
        pageTitle="Changes"
        i18n-pageTitle="@@node.menu.changes">
      </kpn-page-menu-option>

    </kpn-page-menu>

  `
})
export class NodePageHeaderComponent {

  @Input() nodeId: number;
  @Input() nodeName: string;

  linkNodeDetails(): string {
    return this.linkNode("");
  }

  linkNodeMap(): string {
    return this.linkNode("/map");
  }

  linkNodeChanges(): string {
    return this.linkNode("/changes");
  }

  private linkNode(suffix: string): string {
    return `/analysis/node/${this.nodeId}${suffix}`;
  }

}
