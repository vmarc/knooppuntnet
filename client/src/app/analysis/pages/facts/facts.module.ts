import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {FactsPageComponent} from "./_facts-page.component";
import {FactsRoutingModule} from "./facts-routing.module";
import {FactModule} from "../../fact/fact.module";
import {SharedModule} from "../../../components/shared/shared.module";

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    FactModule,
    FactsRoutingModule
  ],
  declarations: [
    FactsPageComponent
  ],
  exports: [
    FactsPageComponent
  ]
})
export class FactsModule {
}
