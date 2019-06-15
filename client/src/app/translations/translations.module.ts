import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {ReactiveFormsModule} from "@angular/forms";
import {
  MatButtonModule, MatCheckboxModule,
  MatDividerModule,
  MatFormFieldModule,
  MatInputModule,
  MatPaginatorModule,
  MatRadioModule,
  MatSortModule,
  MatTableModule
} from "@angular/material";
import {TranslationTableComponent} from "./translation-table.component";
import {TranslationsRoutingModule} from "./translations-routing.module";
import {TranslationsComponent} from "./translations.component";
import {TranslationsService} from "./translations.service";
import {TranslationsLoadComponent} from "./translations-load.component";
import {TranslationsEditComponent} from "./translations-edit.component";
import {TranslationLocationComponent} from "./translation-location.component";
import {TranslationUnitComponent} from "./translation-unit.component";
import {TranslationLocationsComponent} from "./translation-locations.component";

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatPaginatorModule,
    MatSortModule,
    MatTableModule,
    MatInputModule,
    MatFormFieldModule,
    MatDividerModule,
    MatButtonModule,
    TranslationsRoutingModule,
    MatRadioModule,
    MatCheckboxModule
  ],
  declarations: [
    TranslationsComponent,
    TranslationUnitComponent,
    TranslationLocationComponent,
    TranslationLocationsComponent,
    TranslationsLoadComponent,
    TranslationsEditComponent,
    TranslationTableComponent
  ],
  providers: [
    TranslationsService
  ],
})
export class TranslationsModule {
}
