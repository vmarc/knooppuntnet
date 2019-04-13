import {NgModule} from "@angular/core";
import {MatIconModule} from "@angular/material";
import {PdfService} from "./pdf.service";

@NgModule({
  imports: [
    MatIconModule
  ],
  declarations: [],
  exports: [
  ],
  providers: [
    PdfService
  ]
})
export class PdfModule {
}
