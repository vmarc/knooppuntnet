import { jsPDF } from 'jspdf';

export class PdfColourBox {
  knownColours = [
    'aqua',
    'black',
    'blue',
    'brown',
    'gray',
    'green',
    'grey',
    'orange',
    'purple',
    'red',
    'white',
    'yellow',
  ];

  constructor(
    private doc: jsPDF,
    private x: number,
    private y: number,
    private size: number,
    private colourTagValue: string
  ) {}

  print(): void {
    const colourSets = this.colourTagValue
      .split(';')
      .map((tabSubValue) =>
        tabSubValue
          .split('-')
          .filter((tagColour) => this.knownColours.includes(tagColour))
      )
      .filter((c) => c.length > 0);

    if (colourSets.length > 0) {
      const colours = colourSets[0];
      const colourHeight = this.size / colours.length;
      colours.forEach((colour, index) => {
        this.doc.setFillColor(colours[index]);
        this.doc.rect(
          this.x,
          this.y + colourHeight * index,
          this.size,
          colourHeight,
          'F'
        );
      });
      this.doc.rect(this.x, this.y, this.size, this.size, 'S');
    }
  }
}
