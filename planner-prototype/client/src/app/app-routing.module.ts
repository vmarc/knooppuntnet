import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {PdfResolver} from "./resolver/pdf-resolver";
import {ContainerComponent, ExportCompactComponent, ExportPdfComponent, ExportRouteComponent, FaqContainerComponent} from "./container";

const routes: Routes = [
  {path: "", component: ContainerComponent},
  {path: "faq", component: FaqContainerComponent},
  {path: "export", component: ExportRouteComponent},
  {path: "export/pdf", component: ExportPdfComponent, resolve: {pdfDocument: PdfResolver}},
  {path: "export/compact", component: ExportCompactComponent},
  {path: "", redirectTo: "/", pathMatch: "full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [PdfResolver]
})
export class AppRoutingModule {
}
