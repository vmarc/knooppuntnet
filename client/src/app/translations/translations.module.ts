import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {ReactiveFormsModule} from "@angular/forms";
import {MatButtonModule, MatDividerModule, MatFormFieldModule, MatInputModule, MatPaginatorModule, MatSortModule, MatTableModule} from "@angular/material";
import {TranslationTableComponent} from "./translation-table/translation-table.component";
import {TranslationsRoutingModule} from "./translations-routing.module";
import {TranslationsComponent} from "./translations.component";
import {TranslationsService} from "./translations.service";

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
