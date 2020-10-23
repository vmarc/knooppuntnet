import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {StoreModule} from "@ngrx/store";
import {DemoEffects} from "./demo/demo.effects";
import {EffectsModule} from "@ngrx/effects";
import {StoreDevtoolsModule} from "@ngrx/store-devtools";
import {environment} from "../../environments/environment";
import {metaReducers, reducers} from "./core.state";

@NgModule({
  imports: [
    CommonModule,
    StoreModule.forRoot(reducers, {metaReducers}),
    EffectsModule.forRoot([
      DemoEffects
    ]),
    environment.production
      ? []
      : StoreDevtoolsModule.instrument({
        name: "Knooppuntnet"
      })
  ]
})
export class CoreModule {
}
