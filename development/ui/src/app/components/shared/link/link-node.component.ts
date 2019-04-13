import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-node",
  template: `
    <a routerLink="{{'/analysis/node/' + nodeId}}">{{title}}</a>
  `
})
export class LinkNodeComponent {
  @Input() nodeId: string;
  @Input() title: string;
}
