import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {SharedModule} from "../../components/shared/shared.module";
import {AnalysisComponentsModule} from "../components/_analysis-components.module";
import {ChangesService} from "../components/changes/filter/changes.service";
import {ChangesPageComponent} from "./_changes-page.component";
import {ChangesRoutingModule} from "./_changes-routing.module";

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    AnalysisComponentsModule,
    ChangesRoutingModule
  ],
  declarations: [
    ChangesPageComponent
  ],
  providers: [
    ChangesService
  ]
})
export class ChangesModule {
}
