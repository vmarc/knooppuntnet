import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TranslationsComponent} from "./translations.component";
import {TranslationsService} from "./translations.service";
import {TranslationsRoutingModule} from "./translations-routing.module";
import {KpnMaterialModule} from "../material/kpn-material.module";
import {ReactiveFormsModule} from "@angular/forms";
import {TranslationTableComponent} from "./translation-table/translation-table.component";

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    KpnMaterialModule,
    TranslationsRoutingModule
  ],
  declarations: [
    TranslationsComponent,
    TranslationTableComponent
  ],
  providers: [
    TranslationsService
  ],
})
export class TranslationsModule {
}
