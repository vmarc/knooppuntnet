import {Component, Input} from "@angular/core";

@Component({
  selector: "josm-node",
  template: `<josm-link kind="node" id="{{nodeId}}"></josm-link>`
})
export class JosmNodeComponent {
  @Input() nodeId = "";
}
