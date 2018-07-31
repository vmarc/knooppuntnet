import {Component, Input} from "@angular/core";
import {Reference} from "../../kpn/shared/common/reference";

@Component({
  selector: 'icon-route-link',
  template: `
    <icon-link [reference]="route" [url]="'/analysis/route/' + route.id"></icon-link>
  `
})
export class IconRouteLinkComponent {
  @Input() route: Reference;
}
