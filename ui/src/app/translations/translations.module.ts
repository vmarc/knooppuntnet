import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TranslationsComponent} from "./translations.component";
import {TranslationsService} from "./translations.service";
import {TranslationsRoutingModule} from "./translations-routing.module";

@NgModule({
  imports: [
    CommonModule,
    TranslationsRoutingModule
  ],
  declarations: [
    TranslationsComponent
  ],
  providers: [
    TranslationsService
  ],
})
export class TranslationsModule {
}
