import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {ReactiveFormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatDividerModule} from "@angular/material/divider";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatRadioModule} from "@angular/material/radio";
import {MatSortModule} from "@angular/material/sort";
import {MatTableModule} from "@angular/material/table";
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
    MatButtonModule,
    MatCheckboxModule,
    MatDividerModule,
    MatFormFieldModule,
    MatInputModule,
    MatPaginatorModule,
    MatRadioModule,
    MatSortModule,
    MatTableModule,
    TranslationsRoutingModule
  ],
  declarations: [
    TranslationsComponent,
    TranslationsLoadComponent,
    TranslationsEditComponent,
    TranslationUnitComponent,
    TranslationLocationComponent,
    TranslationLocationsComponent,
    TranslationTableComponent
  ],
  providers: [
    TranslationsService
  ],
})
export class TranslationsModule {
}
