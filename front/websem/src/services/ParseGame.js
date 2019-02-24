const typeAssoc = {
    "http://www.videogame-project.fr/2019/videoGameOntology.owl#TripleA" : "TripleA",
    "http://www.videogame-project.fr/2019/videoGameOntology.owl#VideoGame" : "VideoGame",
    "http://www.videogame-project.fr/2019/videoGameOntology.owl#Inde" : "Inde",
    "http://www.videogame-project.fr/2019/videoGameOntology.owl#Retro" : "Retro"
};


const propertyMapping = {
    "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" : (obj, videoGame) => {
          if (videoGame.types === undefined){
              videoGame.types = [];
          }

          videoGame.types.push(typeAssoc[obj.uri])
    },
    "http://www.w3.org/2002/07/owl#sameAs" : (obj, videoGame) => {
        videoGame.uri = obj.literal.content;
        videoGame.id = videoGame.uri.split("#")[1]
    },
    "http://www.wikidata.org/prop/direct/P577" : (obj, videoGame) => {
        videoGame.publication = obj.literal.content;
    },
    "http://www.wikidata.org/prop/direct/hasGenre" : (obj, videoGame) => {
        if (videoGame.genres === undefined){
            videoGame.genres = [];
        }

        videoGame.genres.push(obj.uri.split('#')[1])
    },
    "http://www.wikidata.org/prop/direct/hasName" : (obj, videoGame) => {
        videoGame.name = obj.literal.content;
    },
    "http://www.wikidata.org/prop/direct/hasUserScore" : (obj, videoGame) => {
        videoGame.score = obj.literal.content;
    },
    "http://www.wikidata.org/prop/direct/numberOfsells" : (obj, videoGame) => {
        videoGame.sells = obj.literal.content;
    },
    "http://www.wikidata.org/prop/direct/topOfYear" : (obj, videoGame) => {
        videoGame.top = obj.literal.content;
    },
};

const varMapping = {
    game : (obj, videoGame) => {
        videoGame.uri = obj.uri;
        videoGame.id = videoGame.uri.split("#")[1]
    },
    name : (obj, videoGame) => {
        videoGame.name = obj.literal.content
    }
};

export default {
    parseGame(obj){
        let game = {};
        obj = obj.sparql.results.result;

        for (let property of obj){
            if(property.binding[0].uri in propertyMapping){
                propertyMapping[property.binding[0].uri](property.binding[1],game)
            }
        }

        return game;
    },

    parseMultipleGames(obj){
        let games = [];

        obj = obj.sparql.results.result;

        for(let gameProperty of obj){
            let game = {};
            for(let property of gameProperty.binding){
                if(property.name in varMapping){
                    varMapping[property.name](property,game)
                }
            }
            games.push(game)
        }
        return games;
    }
}