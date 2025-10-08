import { Injectable } from '@angular/core';
import { Plan } from '../domain/plan/plan';
import { GpxWriter } from './plan/gpx-writer';
import { PdfDocument } from './plan/pdf-document';
import { PdfStripDocument } from './plan/pdf-strip-document';
import { PdfTextDocument } from './plan/pdf-text-document';

@Injectable()
export class PdfService {
  printDocument(plan: Plan, planUrl: string, name: string, qrCode: any): void {
    new PdfDocument(plan, planUrl, name, qrCode).print();
  }

  printStripDocument(plan: Plan, name: string): void {
    new PdfStripDocument(plan, name).print();
  }

  printTextDocument(plan: Plan, name: string): void {
    new PdfTextDocument(plan, name).print();
  }

  writeGpx(plan: Plan, name: string): void {
    new GpxWriter().write(plan, name);
  }
}
