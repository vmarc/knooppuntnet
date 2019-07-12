import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-node-page-header",
  template: `

    <kpn-page-header subject="node-page">
      <span>Node</span>
      <span *ngIf="nodeName">&nbsp;{{nodeName}}</span>
    </kpn-page-header>

    <kpn-page-menu>

      <kpn-page-menu-option
        pageName="node"
        [selectedPageName]="pageName"
        [link]="linkNodeDetails()"
        pageTitle="Details">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="node-map"
        [selectedPageName]="pageName"
        [link]="linkNodeMap()"
        pageTitle="Map">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="node-changes"
        [selectedPageName]="pageName"
        [link]="linkNodeChanges()"
        pageTitle="Changes">
      </kpn-page-menu-option>

    </kpn-page-menu>

  `
})
export class NodePageHeaderComponent {

  @Input() nodeId;
  @Input() nodeName;
  @Input() pageName;

  link(pageName: string): string {
    return "/analysis/" + pageName + "/" + this.nodeId;
  }

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
