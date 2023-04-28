import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatRadioModule } from '@angular/material/radio';
import { SharedModule } from '@app/components/shared';
import { AnalysisStrategyModule } from '../strategy';
import { AnalysisRoutingModule } from './analysis-routing.module';
import { AnalysisSidebarComponent } from './analysis-sidebar.component';
import { AnalysisCanoePageComponent } from './pages/analysis-canoe-page.component';
import { AnalysisCyclingPageComponent } from './pages/analysis-cycling-page.component';
import { AnalysisHikingPageComponent } from './pages/analysis-hiking-page.component';
import { AnalysisHorseRidingPageComponent } from './pages/analysis-horse-riding-page.component';
import { AnalysisInlineSkatingPageComponent } from './pages/analysis-inline-skating-page.component';
import { AnalysisMotorboatPageComponent } from './pages/analysis-motorboat-page.component';
import { AnalysisPageComponent } from './pages/analysis-page.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    MatRadioModule,
    MatIconModule,
    AnalysisRoutingModule,
    AnalysisStrategyModule,
    AnalysisPageComponent,
    AnalysisCyclingPageComponent,
    AnalysisHikingPageComponent,
    AnalysisInlineSkatingPageComponent,
    AnalysisMotorboatPageComponent,
    AnalysisHorseRidingPageComponent,
    AnalysisCanoePageComponent,
    AnalysisSidebarComponent,
  ],
})
export class AnalysisModule {}
