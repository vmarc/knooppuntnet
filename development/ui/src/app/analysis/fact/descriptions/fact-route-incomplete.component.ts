import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'kpn-fact-route-incomplete',
  template: `
    <!--De route is onvolledig. De routes zijn uitdrukkelijk gemarkeerd als onvolledig door het
    |toevoegen van een tag met sleutel _"fixme"_ en waarde _"incomplete"_ in de route relatie.""".stripMargin
    -->
    MARKED The route is marked as having an incomplete definition. A route definition is explicitely
    marked incomplete by adding a tag _"fixme"_ with value _"incomplete"_ in the route relation.
  `
})
export class FactRouteIncompleteComponent {
}
