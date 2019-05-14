# lp21

Der komplette Lehrplan im einfach wiederverwendbaren Format. 

Vereinfacht die Planung und Bewertung eines Schulkindes für Lehrpersonen ohne spezial Software.

## Features
- [x] Auslesen aller Kompetenzen des luzernen Lehrplans via https://lu.lehrplan.ch
- [x] Exportieren in CSV format
- [x] Exportieren der Kompetenzen als Exel (XLS,ODS) Datei
 * Persönlich: Ersetze 'Schülerinnen und Schüler' durch den Namen deines Kindes
 * Filterbar: Filtere anzuzeigende Kompetenzen nach Fach, Zyklus, ...
- [ ] Publiziere generierte Kompetenzpläne
 * Download Bereich für alle erzeugten Artefakte des Lehrplans
- [ ] Auslesen der Kompetenzen anderer Kantone. Priorisiere klassische Homschooler Kantone (AG, BE, ZH)
- [x] CLI API damit Benutzer selbst ihren kantonalen Lehrplan herunterladen können.
- [x] Zusätzliche Arbeitsmappen zum Planen & Bewerten der Kompetenzen pro Kind/Semester

## Your own
`java -jar lp21-XYZ.jar ch.monokellabs.lp21.load.LpLoadCli lu.lp21.starts.properties ag csv|xls`

Parameter:
1. Datei im Properties Format, welches den Code ersten Kompetenz eines Faches referenziert.
2. Kürzel des Kantons z.B. ag,lu oder zh.
3. Export Format `csv` oder `xls`.

## Legal
- Open Source
- Crawling ist in der Schweiz legal

> Soli deo gloria