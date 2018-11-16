import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-analysis-failed',
  template: `
    <!--Er heeft zich een probleem voorgedaan tijdens de route analyse. De route is waarschijnlijk te complex.-->
    The route could not be analyzed (too complex?).
  `
})
export class FactRouteAnalysisFailedComponent {
}
