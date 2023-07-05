import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { OsmLinkRelationComponent } from '@app/components/shared/link';
import { SymbolExample } from './symbol-example';
import { SymbolShape } from './symbol-shape';
import { SymbolComponent } from './symbol.component';

@Component({
  selector: 'kpn-status-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <div class="page">
      <h1>Symbols</h1>
      <h2>Background shapes</h2>
      <div class="symbols">
        <div *ngFor="let symbol of backgroundSymbols" class="symbol">
          <div class="symbol-name">{{ symbol }}</div>
          <kpn-symbol
            [description]="backgroundShapeDescription(symbol)"
            [grid]="true"
          ></kpn-symbol>
        </div>
      </div>
      <h2>Foreground shapes</h2>
      <div class="symbols">
        <div *ngFor="let symbol of symbols" class="symbol">
          <div class="symbol-name">{{ symbol }}</div>
          <kpn-symbol
            [description]="foregroundShapeDescription(symbol)"
            [grid]="true"
          ></kpn-symbol>
        </div>
      </div>
      <h2>Wiki examples</h2>
      <div>
        <div *ngFor="let symbol of wikiExamples" class="example">
          <div class="symbol-name">{{ symbol }}</div>
          <kpn-symbol [description]="symbol" [grid]="true"></kpn-symbol>
        </div>
      </div>
      <h2>Node network examples</h2>
      <div>
        <div *ngFor="let example of nodeNetworkExamples" class="example">
          <div class="symbol-name">
            <span>{{ example.description }}</span>
            <a [routerLink]="'/analysis/route/' + example.relationId">
              {{ example.relationId }}
            </a>
          </div>
          <kpn-symbol
            [description]="example.description"
            [grid]="true"
          ></kpn-symbol>
        </div>
      </div>
      <h2>Monitor examples</h2>
      <div>
        <div *ngFor="let example of monitorExamples" class="example">
          <div class="symbol-name">
            <span>{{ example.description }} (relation </span>
            <kpn-osm-link-relation
              [relationId]="example.relationId"
              [title]="example.relationId.toString()"
            />
            <span>)</span>
          </div>
          <kpn-symbol
            [description]="example.description"
            [grid]="true"
          ></kpn-symbol>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .page {
        padding: 1em;
        width: 100%;
      }

      .symbols {
        display: flex;
        flex-wrap: wrap;
      }

      .symbol {
        padding: 0.5em;
        border: 1px solid lightgray;
        margin: 0 0 1em 1em;
        width: 6em;
      }

      .symbol-name {
        padding-bottom: 0.5em;
        font-size: 14px;
      }

      .example {
        margin: 0 0 1em 1em;
      }
    `,
  ],
  standalone: true,
  imports: [NgFor, SymbolComponent, OsmLinkRelationComponent, RouterLink],
})
export class SymbolsComponent {
  readonly symbols = SymbolShape.foregroundShapes;
  readonly backgroundSymbols = SymbolShape.backgroundShapes;

  readonly wikiExamples = [
    'green:white:green_bar',
    'red:white:red_bar:FW:gray',
    'black:black:X29:white',
    'red:white:green_circle:1:black',
    'red:red_dot',
    'blue:blue:shell_modern',
    'green:white:yellow_bar:green_stripe',
    'blue:yellow:white_diamond:blue_diamond_right',
    'blue:blue:white_hiker',
    'blue:green:orange_wheel',
  ];

  // mongodb aggregation to build list with all "osmc:symbol" values used in node network routes:
  // [
  //   {
  //     $unwind: {
  //       path: "$tags.tags",
  //     },
  //   },
  //   {
  //     $match: {
  //       "tags.tags.key": "osmc:symbol",
  //     },
  //   },
  //   {
  //     $project: {
  //       symbol: "$tags.tags.value",
  //     },
  //   },
  //   {
  //     $group: {
  //       _id: "$symbol",
  //     },
  //   },
  // ]
  readonly nodeNetworkExamples: SymbolExample[] = [
    { description: 'blue:white:blue_dot', relationId: 14220917 },
    { description: 'black:white:black_diamond_line', relationId: 14446728 },
    { description: 'blue:white:blue_arrow', relationId: 14588436 },
    { description: 'yellow::yellow_bar::', relationId: 15098029 },
    { description: 'yellow:white:yellow_lower::', relationId: 13603403 },
    { description: 'red:white:red_bar', relationId: 15956463 },
    { description: 'red:white:red_circle', relationId: 15797660 },
    { description: 'red:yellow:blue_diamond', relationId: 11486632 },
    { description: 'red:white:red_diamond', relationId: 11486961 },
    { description: 'white:white', relationId: 11187068 },
    { description: 'red:white:red_lower', relationId: 16028257 },
    {
      description: 'green:white:green_diamond_line:S:blue',
      relationId: 2518252,
    },
    { description: 'red:yellow:red_lower:HSG:black', relationId: 5144686 },
    { description: 'grey:white:grey_pointer', relationId: 9678185 },
    { description: 'red:white::GR:red', relationId: 13562669 },
    { description: 'blue:white:blue_diamond:~:white', relationId: 11896348 },
    {
      description: 'blue:blue_diamond_line:white_diamond:s:blue',
      relationId: 11919825,
    },
    { description: 'red:white:red_lower:420:black', relationId: 13587496 },
    { description: 'green:white:lower_green:RO:black', relationId: 11195184 },
    { description: 'red:white:green_diamond:rs:white', relationId: 11486613 },
  ];

  readonly monitorExamples: SymbolExample[] = [
    { description: 'blue:blue:E9:yellow', relationId: 12524022 },
    { description: 'blue:blue:shell_modern', relationId: 1247178 },
    { description: 'blue:white:blue_bar', relationId: 1983045 },
    { description: 'blue:white:red_lower:Nk:black', relationId: 1959162 },
    { description: 'blue:white:yellow_bar', relationId: 4124609 },
    { description: 'blue:yellow:blue_lower::', relationId: 5194526 },
    { description: 'orange:', relationId: 2535650 },
    { description: 'orange:yellow:red_lower::black', relationId: 6391596 },
    { description: 'orange:yellow:red_lower:GH:black', relationId: 6691445 },
    { description: 'red:', relationId: 7413278 },
    { description: 'red:blue::E2:yellow', relationId: 13638 },
    { description: 'red:blue_round:white_hiker', relationId: 5369302 },
    { description: 'red:red:white_stripe:VF:black', relationId: 15976219 },
    { description: 'red:white:red_lower:347:black', relationId: 12909801 },
    { description: 'red:white:red_lower:Antw:black', relationId: 2436557 },
    { description: 'yellow:green:yellow_bar:B01:blue', relationId: 12118545 },
    {
      description: 'yellow:white:yellow_lower:25.1:black',
      relationId: 3743890,
    },
    { description: 'yellow:white:yellow_rectangle', relationId: 12930557 },
  ];

  backgroundShapeDescription(shape: string): string {
    return `black:grey_${shape}`;
  }

  foregroundShapeDescription(shape: string): string {
    return `black::gray_${shape}`;
  }
}
