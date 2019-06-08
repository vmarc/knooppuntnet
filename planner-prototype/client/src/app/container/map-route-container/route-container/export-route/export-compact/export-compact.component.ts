import {Component, OnInit} from "@angular/core";
import {Route} from "../../../../../model";
import {RouteDetailsService} from "../../../../../service";
import {Router} from "@angular/router";
import * as jsPDF from "jspdf";
import * as html2canvas from "html2canvas";

@Component({
  selector: "app-export-compact",
  templateUrl: "./export-compact.component.html",
  styleUrls: ["./export-compact.component.scss"]
})
export class ExportCompactComponent implements OnInit {

  route: Route;

  constructor(private routeDetailsService: RouteDetailsService,
              private router: Router) {
  }

  ngOnInit() {
    this.routeDetailsService.routeObservable.subscribe(response => this.route = response);
  }

  createPdf() {
    let source = window.document.getElementById("exportable");

    html2canvas(source).then(canvas => {
      let img = canvas.toDataURL("image/png");
      let doc = new jsPDF();
      doc.addImage(img, "JPEG", 0, 0, "", "", "", "FAST");
      doc.save("compact.pdf");
    });
  }

  back() {
    this.router.navigate(["/knooppuntnet/export"]);
  }
}
