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
