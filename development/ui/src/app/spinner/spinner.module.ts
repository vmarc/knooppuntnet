import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatProgressSpinnerModule} from "@angular/material";
import {SpinnerComponent} from "./spinner.component";

@NgModule({
  imports: [
    CommonModule,
    MatProgressSpinnerModule
  ],
  declarations: [
    SpinnerComponent
  ],
  exports: [
    SpinnerComponent
  ]
})
export class SpinnerModule {
}
