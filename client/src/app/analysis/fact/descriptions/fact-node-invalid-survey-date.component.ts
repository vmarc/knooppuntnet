import { Component, OnInit } from "@angular/core";

@Component({
  selector: "kpn-fact-node-invalid-survey-date",
  template: `
    <markdown i18n="@@fact.description.node-invalid-survey-date">
      The node survey date (in the _"survey:date"_ or _"source:date"_ tag) has an
      unexpected format (valid format: YYYY-MM-DD or YYYY-MM).
    </markdown>
  `,
  styles: [
  ],
})
export class FactNodeInvalidSurveyDateComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
