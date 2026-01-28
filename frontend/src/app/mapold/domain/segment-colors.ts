export class SegmentColors {
  static readonly colors = [
    '#e6194B', // red
    '#3cb44b', // green
    '#ffe119', // yellow
    '#4363d8', // blue
    '#f58231', // orange
    '#911eb4', // purple
    '#42d4f4', // cyan
    '#f032e6', // magenta
    '#bfef45', // lime
    '#fabed4', // pink
    '#469990', // teal
    '#dcbeff', // lavender
    '#9A6324', // brown
    '#fffac8', // beige
    '#800000', // maroon
    '#aaffc3', // mint
    '#808000', // olive
    '#ffd8b1', // apricot
    '#000075', // navy
  ];

  static colorForSegmentId(id: number): string {
    const index = (id - 1) % this.colors.length;
    return this.colors[index];
  }
}
