import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PageComponent} from "./page/page.component";
import {KpnMaterialModule} from "../material/kpn-material.module";

@NgModule({
  imports: [
    CommonModule,
    KpnMaterialModule
  ],
  declarations: [
    PageComponent
  ],
  exports: [
    PageComponent
  ]
})
export class SharedModule {
}
