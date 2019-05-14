import {NgModule} from "@angular/core";
import {MatIconModule} from "@angular/material";
import {BitmapIconService} from "./bitmap-icon.service";
import {PdfService} from "./pdf.service";

@NgModule({
  imports: [
    MatIconModule
  ],
  declarations: [],
  exports: [
  ],
  providers: [
    PdfService,
    BitmapIconService
  ]
})
export class PdfModule {
}
