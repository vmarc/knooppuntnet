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
        [active]="pageName == 'details'"
        i18n="@@node.menu.details">
        Details
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkNodeMap()"
        [state]="state()"
        [active]="pageName == 'map'"
        i18n="@@node.menu.map">
        Map
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkNodeChanges()"
        [state]="state()"
        [active]="pageName == 'changes'"
        [elementCount]="changeCount"
        i18n="@@node.menu.changes">
        Changes
      </kpn-page-menu-option>

    </kpn-page-menu>
  `
})
export class NodePageHeaderComponent {

  @Input() nodeId: string;
  @Input() nodeName: string;
  @Input() changeCount: number;
  @Input() pageName: string;

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
    return {nodeName: this.nodeName, changeCount: this.changeCount};
  }

  private linkNode(suffix: string): string {
    return `/analysis/node/${this.nodeId}${suffix}`;
  }

}
