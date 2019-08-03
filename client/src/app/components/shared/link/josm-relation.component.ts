import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-josm-relation",
  template: `<kpn-josm-link kind="relation" id="{{relationId}}" full="true"></kpn-josm-link>`
})
export class JosmRelationComponent {
  @Input() relationId: number;
}
