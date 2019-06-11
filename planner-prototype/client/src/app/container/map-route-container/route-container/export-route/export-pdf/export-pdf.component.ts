import {Component, OnInit} from "@angular/core";
import {PdfDocument} from "../../../../../model";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import * as jsPDF from "jspdf";
import * as html2canvas from "html2canvas";

@Component({
  selector: "app-export-pdf",
  templateUrl: "./export-pdf.component.html",
  styleUrls: ["./export-pdf.component.scss"]
})
export class ExportPdfComponent implements OnInit {

  pdfDocument: PdfDocument;
  timeRepresentation: string;

  constructor(private route: ActivatedRoute,
              private spinner: NgxSpinnerService,
              private router: Router) {
  }

  ngOnInit() {
    this.pdfDocument = this.route.snapshot.data["pdfDocument"];
    this.timeRepresentation = this.pdfDocument.totalHoursParsed + " h " +
      this.pdfDocument.totalMinutesParsed + " m " +
      this.pdfDocument.totalSecondsParsed + " s";
  }

  createPdf() {
    this.spinner.show();
    const source = window.document.getElementById("exportable");

    html2canvas(source, {logging: false}).then(canvas => {
      const imgData = canvas.toDataURL("image/png");
      const imgWidth = 210;
      const pageHeight = 295;
      console.log(canvas);
      const imgHeight = canvas.height * imgWidth / canvas.width;
      let heightLeft = imgHeight;

      const doc = new jsPDF("p", "mm", "a4", true);
      let position = 0;

      doc.addImage(imgData, "PNG", 0, position, imgWidth, imgHeight, "", "FAST");
      heightLeft -= pageHeight;

      while (heightLeft >= 0) {
        position = heightLeft - imgHeight;
        doc.addPage();
        doc.addImage(imgData, "PNG", 0, position, imgWidth, imgHeight, "", "FAST");
        heightLeft -= pageHeight;
      }
      this.spinner.hide();
      doc.save("file.pdf")﻿;
    });
  }

  back() {
    this.router.navigate(["/knooppuntnet/export"]);
  }

  convertTime(time: number): string {
    const convertToSeconds = Math.floor(time / 1000);
    const minutes = Math.floor(convertToSeconds / 60);
    const seconds = convertToSeconds - (minutes * 60);

    return minutes + " m " + seconds + " s";
  }
}
