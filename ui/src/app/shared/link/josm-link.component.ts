import {Component, Input} from "@angular/core";

@Component({
  selector: 'josm-link',
  template: `
    <a
      rel="nofollow"
      href="http://localhost:8111/import?url=https://api.openstreetmap.org/api/0.6/{{kind}}/{{id}}{{full ? 'full' : ''}}">
      edit
    </a>
  `
})
export class JosmLinkComponent {
  @Input() kind = "";
  @Input() id = "";
  @Input() full = false;
}
