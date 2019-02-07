import {Component, Input} from "@angular/core";

@Component({
  selector: 'josm-relation',
  template: `<josm-link kind="relation" id="{{relationId}}" full="true"></josm-link>`
})
export class JosmRelationComponent {
  @Input() relationId = "";
}
