import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-josm-relation",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<kpn-josm-link kind="relation" [elementId]="relationId" [full]="true"></kpn-josm-link>`
})
export class JosmRelationComponent {
  @Input() relationId: number;
}
