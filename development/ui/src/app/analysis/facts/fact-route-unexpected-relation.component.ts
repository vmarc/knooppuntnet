import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-unexpected-relation',
  template: `
    <!--
    |De route relatie bevat 1 of meer overwachte relaties. In route relaties verwachten we enkel
    |onderdelen van het type _"way"_, of onderdelen van het type _"node"_ met een tag met sleutel
    |_"rwn_ref"_ of _"rcn_ref"_.""".stripMargin
    -->
    MARKED The route relation contains one or more unexpected relation members. In route relations
    we expect only members of type _"way"_, or members of type _"node"_ with a tag
    _"rwn_ref"_ or _"rcn_ref"_.
  `
})
export class FactRouteUnexpectedRelationComponent {
}
