pandoc osm-segment-report.md \
  -o osm-segment-report.pdf \
  --pdf-engine=xelatex \
  -f gfm \
  -V mainfont="DejaVu Serif" \
  -V fontsize=8pt \
  -V geometry:a4paper \
  -V geometry:margin=2cm \
  -H osm-segment-report.tex
