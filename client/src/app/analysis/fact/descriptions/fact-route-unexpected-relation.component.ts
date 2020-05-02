import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-unexpected-relation",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-unexpected-relation">
      The route relation contains one or more unexpected relation members. In route relations
      we expect only members of type _"way"_, or members of type _"node"_ with a tag
      _"rwn_ref"_ or _"rcn_ref"_.
    </markdown>
  `
})
export class FactRouteUnexpectedRelationComponent {
}
