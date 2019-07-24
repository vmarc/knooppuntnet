import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-node",
  template: `
    <a [routerLink]="['/analysis/node', nodeId]" [state]="{nodeName: nodeName}">{{nodeName}}</a>
  `
})
export class LinkNodeComponent {
  @Input() nodeId: number;
  @Input() nodeName: string;
}
