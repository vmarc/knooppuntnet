import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-tag-invalid',
  template: `
    <markdown ngPreserveWhitespaces i18n="@@fact.description.route-tag-invalid">
      Invalid value in required tag _"route"_ in route relation.

      A bicycle route relation needs to have value _"bicycle"_ in its _"route"_ tag.

      A hiking route relation needs to have one of the following values in its _"route"_ tag: _"foot"_,
      _"hiking"_, or _"walking"_. Note that _"walking"_ is a value that is frequently found,
      but not actually documented as a valid value in the OSM wiki pages.
    </markdown>
  `
})
export class FactRouteTagInvalidComponent {
}
