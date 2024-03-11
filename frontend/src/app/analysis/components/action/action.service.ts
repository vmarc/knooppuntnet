import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { TimeoutComponent } from '@app/components/shared/link';
import { ApiService } from '@app/services';

@Injectable({
  providedIn: 'root',
})
export class ActionService {
  private readonly apiService = inject(ApiService);
  private readonly dialog = inject(MatDialog);

  idNode(nodeId: number): void {
    this.id('node', nodeId);
  }

  idWay(wayId: number): void {
    this.id('way', wayId);
  }

  idRelation(relationId: number): void {
    this.id('relation', relationId);
  }

  osmNode(nodeId: number): void {
    this.osm('node', nodeId);
  }

  osmWay(wayId: number): void {
    this.osm('way', wayId);
  }

  osmRelation(relationId: number): void {
    this.osm('relation', relationId);
  }

  deepHistoryNode(nodeId: number): void {
    this.deepHistory('node', nodeId);
  }

  deepHistoryWay(wayId: number): void {
    this.deepHistory('way', wayId);
  }

  deepHistoryRelation(relationId: number): void {
    this.deepHistory('relation', relationId);
  }

  josmLoadNode(nodeId: number): void {
    this.josmLoad(`node${nodeId}`);
  }

  josmLoadWay(wayId: number): void {
    this.josmLoad(`way${wayId}`);
  }

  josmLoadRelation(relationId: number): void {
    this.josmLoad(`relation${relationId}`);
  }

  josmLoadRelationAndMembers(relationId: number): void {
    this.josmLoad(`relation${relationId}&relation_members=true`);
  }

  josmZoomNode(nodeId: number): void {
    this.josmZoom(`node${nodeId}`);
  }

  josmZoomWay(wayId: number): void {
    this.josmZoom(`way${wayId}`);
  }

  josmZoomRelation(relationId: number): void {
    this.josmZoom(`relation${relationId}`);
  }

  private id(kind: string, elementId: number): void {
    window.open(`https://www.openstreetmap.org/edit?editor=id&${kind}=${elementId}`);
  }

  private osm(kind: string, elementId: number): void {
    window.open(`https://www.openstreetmap.org/${kind}/${elementId}`);
  }

  private deepHistory(kind: string, elementId: number): void {
    window.open(`https://osmlab.github.io/osm-deep-history/#/${kind}/${elementId}`);
  }

  private josmLoad(objectString: string): void {
    const url = `http://localhost:8111/load_object?objects=${objectString}`;
    this.josmCommand(url);
  }

  josmZoom(objectString: string): void {
    const url = `http://localhost:8111/zoom?left=0&right=0&top=0&bottom=0&select=${objectString}`;
    this.josmCommand(url);
  }

  private josmCommand(url: string): void {
    this.apiService.edit(url).subscribe({
      next: (result) => {},
      error: (err) => {
        this.dialog.open(TimeoutComponent, { autoFocus: false, maxWidth: 500 });
      },
    });
  }
}
