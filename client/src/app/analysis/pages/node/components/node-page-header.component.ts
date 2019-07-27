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
        [state]="state()"
        i18n="@@node.menu.details">
        Details
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkNodeMap()"
        [state]="state()"
        i18n="@@node.menu.map">
        Map
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkNodeChanges()"
        [state]="state()"
        i18n="@@node.menu.changes">
        Changes
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

  state(): { [k: string]: any; } {
    return {nodeName: this.nodeName};
  }

  private linkNode(suffix: string): string {
    return `/analysis/node/${this.nodeId}${suffix}`;
  }

}
