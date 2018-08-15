import {Component, Input} from "@angular/core";

@Component({
  selector: 'josm-way',
  template: `<josm-link kind="way" id="{{wayId}}"></josm-link>`
})
export class JosmWayComponent {
  @Input() wayId = "";
}
