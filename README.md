# Projet Final Web Sémantique

Ce projet consiste à appliquer les techniques du Web sémantique afin de 
trouver des similarité entre les jeux video pour effectuer des recommendations.

Ce projet est composé d'un backend en JAVA et un frontend en Vue.js.

## Déployer l'application

### Déployer le backend
java 10 et maven sont requis

```
$ mvn clean package install
$ mvn exec:java -Dexec.mainClass="Main"
```

### Déployer le frontend
npm est requis

```
$ cd front/websem
$ npm install
$ npm run serve
```

## Ressouces

Vous pouvez trouver différentes ressources dans src/main/resources.

- instances.rdf : ce fichier contient toutes les instances des jeux vidéo et genres avec les triplets rdf
- videoGameOntology.owl : ce fichier contient l'ontologie en owl
- videoGameRules.rul : ce fichier contient les règles construct sparql
- create_instances.py : script permettant d'extraire des triplets rdf à partir des données extraites de SteamSpy
- categories.rdf : contient la hiérachie des genres sous forme de triplet rdf