import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-josm-node",
  template: `<kpn-josm-link kind="node" [elementId]="nodeId"></kpn-josm-link>`
})
export class JosmNodeComponent {
  @Input() nodeId: number;
}
