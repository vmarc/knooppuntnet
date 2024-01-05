import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { List } from 'immutable';
import { Plan } from '../domain/plan/plan';
import { PlanInstruction } from '../domain/plan/plan-instruction';
import { GpxWriter } from './plan/gpx-writer';
import { PdfDirections } from './plan/pdf-directions';
import { PdfDocument } from './plan/pdf-document';
import { PdfStripDocument } from './plan/pdf-strip-document';
import { PdfTextDocument } from './plan/pdf-text-document';
import { BitmapIconService } from './services/bitmap-icon.service';

@Injectable()
export class PdfService {
  private readonly iconService = inject(BitmapIconService);

  printDocument(plan: Plan, planUrl: string, name: string, qrCode: any): void {
    new PdfDocument(plan, planUrl, name, qrCode).print();
  }

  printStripDocument(plan: Plan, name: string): void {
    new PdfStripDocument(plan, name, this.iconService).print();
  }

  printTextDocument(plan: Plan, name: string): void {
    new PdfTextDocument(plan, name).print();
  }

  printInstructions(instructions: List<PlanInstruction>, name: string): void {
    new PdfDirections(instructions, this.iconService, name).print();
  }

  writeGpx(plan: Plan, name: string): void {
    new GpxWriter().write(plan, name);
  }
}
