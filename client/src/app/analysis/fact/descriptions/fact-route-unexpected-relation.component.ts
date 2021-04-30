import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-fact-route-unexpected-relation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-unexpected-relation">
      The route relation contains one or more unexpected relation members. In
      route relations we expect only members of type *"way"*, or members of type
      *"node"* with a tag *"rwn_ref"* or *"rcn_ref"*.
    </markdown>
  `,
})
export class FactRouteUnexpectedRelationComponent {}
