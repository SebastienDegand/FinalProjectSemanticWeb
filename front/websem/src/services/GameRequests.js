const parser = require('./ParseGame');

const baseUrl = "http://localhost:4567";

export default {
    async getAllVideoGames(page){
        let req =await fetch(baseUrl + '/videogames?format=json&page='+page, {
            method : 'GET'
        });
        let json = await req.json();
        return parser.default.parseMultipleGames(json);
    },

    async getGameById(id){
        let req = await fetch(baseUrl +'/videogames/' + id + '?format=json', {
            method : 'GET'
        });
        return [parser.default.parseGame(await req.json())];
    },

    async getRecommendation(id){
        let req = await fetch(baseUrl + '/recommendation/' + id + '?format=json', {
            method : 'GET'
        });
        let json = await req.json();

        return parser.default.parseMultipleGames(json);
    }
}