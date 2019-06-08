import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {PdfResolver} from "./resolver/pdf-resolver";
import {ContainerComponent, ExportCompactComponent, ExportPdfComponent, ExportRouteComponent, FaqContainerComponent} from "./container";

const routes: Routes = [
  {path: "knooppuntnet", component: ContainerComponent},
  {path: "knooppuntnet/faq", component: FaqContainerComponent},
  {path: "knooppuntnet/export", component: ExportRouteComponent},
  {path: "knooppuntnet/export/pdf", component: ExportPdfComponent, resolve: {pdfDocument: PdfResolver}},
  {path: "knooppuntnet/export/compact", component: ExportCompactComponent},
  {path: "", redirectTo: "/knooppuntnet", pathMatch: "full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [PdfResolver]
})
export class AppRoutingModule {
}
