import { Injectable } from '@angular/core';
import { PdfTextDocument } from '@app/pdf/plan/pdf-text-document';
import { DirectionsAnalyzer } from '@app/planner/domain/directions/directions-analyzer';
import { Plan } from '@app/planner/domain/plan/plan';
import { PlannerService } from '../planner/services/planner.service';
import { BitmapIconService } from './bitmap-icon.service';
import { PdfDirections } from './plan/pdf-directions';
import { PdfDocument } from './plan/pdf-document';
import { PdfStripDocument } from './plan/pdf-strip-document';

@Injectable()
export class PdfService {
  constructor(
    private iconService: BitmapIconService,
    private plannerService: PlannerService
  ) {}

  printDocument(plan: Plan, planUrl: string, name: string, qrCode: any): void {
    new PdfDocument(plan, planUrl, name, qrCode).print();
  }

  printStripDocument(plan: Plan, name: string): void {
    new PdfStripDocument(plan, name, this.iconService).print();
  }

  printTextDocument(plan: Plan, name: string): void {
    new PdfTextDocument(plan, name, this.plannerService).print();
  }

  printInstructions(plan: Plan, name: string): void {
    const instructions = new DirectionsAnalyzer().analyze(plan);
    new PdfDirections(
      instructions,
      this.iconService,
      this.plannerService,
      name
    ).print();
  }
}
