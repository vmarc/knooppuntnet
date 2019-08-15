import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-analysis-failed",
  template: `
    <p i18n="@@fact.description.route-analysis-failed">
      The route could not be analyzed (too complex?).
    </p>
  `
})
export class FactRouteAnalysisFailedComponent {
}
