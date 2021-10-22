import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatRadioModule } from '@angular/material/radio';
import { SharedModule } from '../../components/shared/shared.module';
import { AnalysisRoutingModule } from './analysis-routing.module';
import { AnalysisCanoePageComponent } from './pages/analysis-canoe-page.component';
import { AnalysisCyclingPageComponent } from './pages/analysis-cycling-page.component';
import { AnalysisHikingPageComponent } from './pages/analysis-hiking-page.component';
import { AnalysisHorseRidingPageComponent } from './pages/analysis-horse-riding-page.component';
import { AnalysisInlineSkatingPageComponent } from './pages/analysis-inline-skating-page.component';
import { AnalysisModeComponent } from './pages/analysis-mode.component';
import { AnalysisModeService } from './pages/analysis-mode.service';
import { AnalysisMotorboatPageComponent } from './pages/analysis-motorboat-page.component';
import { AnalysisPageComponent } from './pages/analysis-page.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    AnalysisRoutingModule,
    MatRadioModule,
    MatIconModule,
  ],
  declarations: [
    AnalysisPageComponent,
    AnalysisCyclingPageComponent,
    AnalysisHikingPageComponent,
    AnalysisInlineSkatingPageComponent,
    AnalysisMotorboatPageComponent,
    AnalysisHorseRidingPageComponent,
    AnalysisCanoePageComponent,
    AnalysisModeComponent,
  ],
  providers: [AnalysisModeService],
})
export class AnalysisModule {}
