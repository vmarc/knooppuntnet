import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {ChangesPageComponent} from "./changes-page.component";
import {SharedModule} from "../../components/shared/shared.module";
import {ChangesRoutingModule} from "./_changes-routing.module";
import {AnalysisComponentsModule} from "../components/_analysis-components.module";

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    AnalysisComponentsModule,
    ChangesRoutingModule
  ],
  declarations: [
    ChangesPageComponent
  ]
})
export class ChangesModule {
}
