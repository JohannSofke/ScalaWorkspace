# Game of Life

[Game of Life](https://de.wikipedia.org/wiki/Conways_Spiel_des_Lebens) ist ein 0 Spieler spiel, in dem sich in einem Rastermuster Leben selbstständig entwickeln kann. In diesem repository habe ich das Spiel in Scala 3 Programmiert.

Interesante seeds
- 112   crawler
- 9 Viele crawler

## Befehle für den Umgang mit dem Programm
### Lasse das Programm laufen
``` 
scala-cli run . -- <seed>
```
### Teste das Programm
```
scala-cli test .
```
### Compiliere das Programm
```
scala-cli compile .
```
### Lösche Compilierartefakte
```
scala-cli clean .

```
### Baue eine ausführbare Datei für die Weitergabe
```
scala-cli --power package . --assembly --preamble=false -o GameOfLife.jar

```

## Besonderheiten der Programmiersprache Scala

### Deklarative Programmierung
Scala untestützt die funktionale Programmierung, eine Form der deklarativen Programmierung. Im Unterschied zur _imperatieven Programmierung_, bei der dem Computer genaue Anweisungen und Schritte zur Erreichung eines Zieles vorgegeben werden, steht bei _deklarativen Programmiersprachen_ die Beschreibung des Endergebnisses im Vordergrund, wobei der konkrete Lösungsweg automatisch ermittelt wird. Hieraus ergeben sich die klaren Vorteile einer deklarativen Programmiersprache:

- Bessere Verständlichkeit
- Kompakterer Code
- Vermeidung von Seiteneffekten
- Verbesserte Testbarkeit durch reine Funktionen

### Typisierte Programmierung
Scala verwendet statische Typisierung. Das bedeutet, das Typen zur Kompilierungszeit überprüft werden und nicht Erst zur Laufzeit. Weiterhin unterstützt Scala Typinferenz (der Compiler leitet die Typen automatisch ab), weshalb man meistens keine Typenangaben machen muss. Hieraus ergeben sich die klaren Vorteile einer typisierten Programmiersprache:

- Fehlerprävention vor Compilierzeit
- Verbesserte Werkzeugnutzung und Codevervollständigung
- Vereinfacht die Weiterentwickung von Projekten

## Scala Projektmanagement
Es gibt folgende relevante Projektmanagement Programme für scala
1. SBT (Simple build tool)
2. Coursier
3. ScalaCLI (Besonders gut für kleine Projekte geeignet)
4. Mill

### project.scala
Project.scala ist die konfigurationsdatei für scala-cli
In project.scala habe ich festgelegt, welche eigenschaften von Scala ich nutzen möchte.
Ich habe dir versionsnummer und die abhängigkeiten zur Bibliothek festgelegt.

## Etwickungsumgebung im Github Codespaces

### devcontainer.json
In dem devcontainer bestimme ich die Konfiguration der Entwicklungsumgebung.

* "name": "Scala-CLI Development", (Name der konfiguration)
* "image" (Betriebssystem der Entwicklungsumgebung)
* "customizations" (Meine Anpassungen für die Entwicklungsumgebung)
* "vscode" (Code Editor)
* "extensions" (Erweiterungen)
* "scalameta.metals" (Erweiterung für den Texteditor, die die arbeit mit der Programmiersprache vereinfacht)
* "postCreateCommand": "curl -fL https://github.com/Virtuslab/scala-cli/releases/latest/download/scala-cli-x86_64-pc-linux.gz | gzip -d > scala-cli && chmod +x scala-cli && sudo mv scala-cli /usr/local/bin/scala-cli", (Lädt das Scala-CLI Programm runter, um Scalaprojekte zu verwalten)
* "postAttachCommand": "scala-cli clean . && scala-cli compile . && scala-cli test ."(Aufrämen, compilieren und Testen des Scalaprojektes)

## Git zur Codeverwaltung
### .gitignore
In gitignore bestimme ich, welche dataien Ordner oder Dateien für git Ignoriert werden sollen.
In dem temporäre Dateien wie /.metals/ werden dort zwischengespeichert.

## Semantic Versioning
Given a version number MAJOR.MINOR.PATCH, increment the:

1. MAJOR version when you make incompatible changes
2. MINOR version when you add functionality in a backward compatible manner
3. PATCH version when you make backward compatible bug fixes
