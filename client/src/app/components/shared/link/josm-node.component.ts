import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-josm-node",
  template: `<kpn-josm-link kind="node" id="{{nodeId}}"></kpn-josm-link>`
})
export class JosmNodeComponent {
  @Input() nodeId: number;
}
