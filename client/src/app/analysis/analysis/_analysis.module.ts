import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {SharedModule} from "../../components/shared/shared.module";
import {AnalysisRoutingModule} from "./_analysis-routing.module";
import {AnalysisBePageComponent} from "./analysis-be-page.component";
import {AnalysisDePageComponent} from "./analysis-de-page.component";
import {AnalysisFrPageComponent} from "./analysis-fr-page.component";
import {AnalysisNlPageComponent} from "./analysis-nl-page.component";
import {AnalysisPageComponent} from "./analysis-page.component";

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    AnalysisRoutingModule,
  ],
  declarations: [
    AnalysisPageComponent,
    AnalysisBePageComponent,
    AnalysisDePageComponent,
    AnalysisFrPageComponent,
    AnalysisNlPageComponent
  ]
})
export class AnalysisModule {
}
