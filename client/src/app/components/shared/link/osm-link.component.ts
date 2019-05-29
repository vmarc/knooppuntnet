import {Component, Input} from "@angular/core";

@Component({
  selector: "osm-link",
  template: `
    <a 
      class="external" 
      rel="nofollow" 
      target="_blank"
      href="https://www.openstreetmap.org/{{kind}}/{{id}}">
      {{title}}
    </a>
  `
})
export class OsmLinkComponent {

  @Input() kind = "";
  @Input() id = "";
  @Input() title = "";

}
