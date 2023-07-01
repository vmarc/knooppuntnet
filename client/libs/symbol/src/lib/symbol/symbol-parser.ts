import { SymbolDescription } from './symbol-description';
import { SymbolShape } from './symbol-shape';

export class SymbolParser {
  private colors = [
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

  private description: SymbolDescription;
  private parts: string[] = [];
  private currentPartIndex = 0;
  private currentPart: string;

  parse(symbolString: string): SymbolDescription {
    if (!symbolString) {
      this.description = { waycolor: 'white' };
      return this.description;
    }
    this.parts = symbolString.split(':');
    if (this.parts.length == 0) {
      this.description = { waycolor: 'white' };
      return this.description;
    }

    this.currentPartIndex = 0;
    this.currentPart = this.parts[this.currentPartIndex];

    this.description = { waycolor: this.currentPart };

    if (this.#hasNextPart()) {
      this.#nextPart();
      this.#parseBackground();
    }

    return this.description;
  }

  #parseBackground(): void {
    const underscoreIndex = this.currentPart.indexOf('_');
    if (underscoreIndex === -1) {
      const color = this.currentPart;
      if (color.length === 0) {
        if (this.#hasNextPart()) {
          this.#nextPart();
          this.#parseForeground();
        }
      } else {
        this.description = {
          ...this.description,
          background: {
            color,
          },
        };
        if (this.#hasNextPart()) {
          this.#nextPart();
          this.#parseForeground();
        }
      }
    } else {
      const shapeIndex = SymbolShape.foregroundShapes.indexOf(this.currentPart);
      if (shapeIndex >= 0) {
        const shape = this.currentPart;
        // shape without color
        this.description = {
          ...this.description,
          foreground: {
            shape,
          },
        };
        if (shape === 'shell' || shape === 'shell_modern') {
          this.description = {
            ...this.description,
            foreground: {
              ...this.description.foreground,
              color: 'yellow',
            },
          };
        }
        if (this.#hasNextPart()) {
          this.#nextPart();
          this.#parseForeground2();
        }
      } else {
        const color = this.currentPart.substring(0, underscoreIndex);
        const shape = this.currentPart.substring(underscoreIndex + 1);
        const backgroundShapeIndex =
          SymbolShape.backgroundShapes.indexOf(shape);
        const foregroundShapeIndex =
          SymbolShape.foregroundShapes.indexOf(shape);

        if (backgroundShapeIndex >= 0) {
          this.description = {
            ...this.description,
            background: {
              color,
              shape,
            },
          };
          if (this.#hasNextPart()) {
            this.#nextPart();
            this.#parseForeground();
          }
        } else if (foregroundShapeIndex >= 0) {
          this.description = {
            ...this.description,
            foreground: {
              color,
              shape,
            },
          };
          if (this.#hasNextPart()) {
            this.#nextPart();
            this.#parseForeground2();
          }
        }
      }
    }
  }

  #parseForeground(): void {
    const shapeIndex = SymbolShape.foregroundShapes.indexOf(this.currentPart);
    if (shapeIndex >= 0) {
      const shape = this.currentPart;
      // shape without color
      this.description = {
        ...this.description,
        foreground: {
          shape,
        },
      };
      if (shape === 'shell' || shape === 'shell_modern') {
        this.description = {
          ...this.description,
          foreground: {
            ...this.description.foreground,
            color: 'yellow',
          },
        };
      }
      if (this.#hasNextPart()) {
        this.#nextPart();
        this.#parseForeground2();
      }
    } else {
      const underscoreIndex = this.currentPart.indexOf('_');
      if (underscoreIndex === -1) {
        const color = this.currentPart;
        if (this.colors.indexOf(color) == -1) {
          this.description = {
            ...this.description,
            text: this.currentPart,
          };
          if (this.#hasNextPart()) {
            this.#nextPart();
            this.#parseTextcolor();
          }
        } else {
          this.description = {
            ...this.description,
            foreground: {
              color,
            },
          };
          if (this.#hasNextPart()) {
            this.#nextPart();
            this.#parseForeground2();
          }
        }
      } else {
        const color = this.currentPart.substring(0, underscoreIndex);
        const shape = this.currentPart.substring(underscoreIndex + 1);
        const foregroundShapeIndex =
          SymbolShape.foregroundShapes.indexOf(shape);
        if (foregroundShapeIndex >= 0) {
          this.description = {
            ...this.description,
            foreground: {
              color,
              shape,
            },
          };
          if (this.#hasNextPart()) {
            this.#nextPart();
            this.#parseForeground2();
          }
        }
      }
    }
  }

  #parseForeground2(): void {
    const shapeIndex = SymbolShape.foregroundShapes.indexOf(this.currentPart);
    if (shapeIndex >= 0) {
      const shape = this.currentPart;
      // shape without color
      this.description = {
        ...this.description,
        foreground: {
          shape,
        },
      };
      if (shape === 'shell' || shape === 'shell_modern') {
        this.description = {
          ...this.description,
          foreground: {
            ...this.description.foreground,
            color: 'yellow',
          },
        };
      }
      if (this.#hasNextPart()) {
        this.#nextPart();
        this.#parseForeground2();
      }
    } else {
      const underscoreIndex = this.currentPart.indexOf('_');
      if (underscoreIndex === -1) {
        const color = this.currentPart;
        if (this.colors.indexOf(color) == -1) {
          this.description = {
            ...this.description,
            text: this.currentPart,
          };
          if (this.#hasNextPart()) {
            this.#nextPart();
            this.#parseTextcolor();
          }
        } else {
          this.description = {
            ...this.description,
            foreground2: {
              color,
            },
          };
          if (this.#hasNextPart()) {
            this.#nextPart();
            this.#parseText();
          }
        }
      } else {
        const color = this.currentPart.substring(0, underscoreIndex);
        const shape = this.currentPart.substring(underscoreIndex + 1);
        const foregroundShapeIndex =
          SymbolShape.foregroundShapes.indexOf(shape);
        if (foregroundShapeIndex >= 0) {
          this.description = {
            ...this.description,
            foreground2: {
              color,
              shape,
            },
          };
          if (this.#hasNextPart()) {
            this.#nextPart();
            this.#parseText();
          }
        }
      }
    }
  }

  #parseText(): void {
    const text = this.currentPart;
    this.description = {
      ...this.description,
      text,
    };
    if (this.#hasNextPart()) {
      this.#nextPart();
      this.#parseTextcolor();
    }
  }

  #parseTextcolor(): void {
    const textcolor = this.currentPart;
    this.description = {
      ...this.description,
      textcolor,
    };
  }

  #hasNextPart() {
    return this.currentPartIndex + 1 < this.parts.length;
  }

  #nextPart() {
    this.currentPartIndex++;
    this.currentPart = this.parts[this.currentPartIndex];
  }
}
