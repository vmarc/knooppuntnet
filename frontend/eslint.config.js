// @ts-check
const eslint = require("@eslint/js");
const tseslint = require("typescript-eslint");
const angular = require("angular-eslint");
const sheriff = require("@softarc/eslint-plugin-sheriff");

module.exports = tseslint.config(
  {
    files: ["**/*.ts"],
    extends: [
      eslint.configs.recommended,
      ...tseslint.configs.recommended,
      ...tseslint.configs.stylistic,
      ...angular.configs.tsRecommended,
      sheriff.configs.all,
    ],
    processor: angular.processInlineTemplates,
    rules: {
      "@angular-eslint/directive-selector": [
        "error",
        {
          type: "attribute",
          prefix: "kpn",
          style: "camelCase",
        },
      ],
      "@angular-eslint/component-selector": [
        "error",
        {
          type: "element",
          prefix: "kpn",
          style: "kebab-case",
        },
      ],
      "@typescript-eslint/no-unused-vars": "off",
      "@typescript-eslint/array-type": "off",
      "@typescript-eslint/prefer-for-of": "off",
      "@typescript-eslint/consistent-generic-constructors": "off",
      "@typescript-eslint/no-explicit-any": "off",
      "@typescript-eslint/ban-ts-comment": "off",
      "@typescript-eslint/adjacent-overload-signatures": "off",
      "@typescript-eslint/no-empty-object-type": "off",
      "@typescript-eslint/consistent-indexed-object-style": "off",
      "@typescript-eslint/consistent-type-definitions": "off",
      "@angular-eslint/no-output-native": "off",
      "@softarc/sheriff/dependency-rule": "warn",
    },
  },
  {
    files: ["**/*.html"],
    ignores: ["**/index.html"],
    extends: [...angular.configs.templateAll, ...angular.configs.templateAccessibility],
    rules: {
      "@angular-eslint/template/eqeqeq": "off",
      "@angular-eslint/template/no-call-expression": "off",
      "@angular-eslint/template/click-events-have-key-events": "off",
      "@angular-eslint/template/interactive-supports-focus": "off",
      "@angular-eslint/template/label-has-associated-control": "off",
      "@angular-eslint/template/attributes-order": "off",
      "@angular-eslint/template/button-has-type": "off",
      "@angular-eslint/template/i18n": [
        "warn",
        {
          ignoreAttributes: [
            "as-split[direction]",
            "as-split[unit]",
            "button[nzShape]",
            "button[nzType]",
            "kpn-icon-button[icon]",
            "kpn-icon-link[elementType]",
            "kpn-location-page-header[pageName]",
            "kpn-osm-link[kind]",
            "kpn-page-header[subject]",
            "nz-divider[nzType]",
            "nz-icon[nzSize]",
            "nz-icon[nzType]",
            "nz-pagination[nzSize]",
            "nz-table[nzPaginationPosition]",
            "nz-table[nzSize]",
            "nzOverlayClassName",
            "rel",
          ],
          ignoreTags: ["mat-icon"],
        },
      ],
      "@angular-eslint/template/no-inline-styles": "off",
      "@angular-eslint/template/prefer-ngsrc": "off",
    },
  }
);
