const baseUrl = "http://localhost:4567";

export default {
    async getAllVideoGames(){
        return await (await fetch(baseUrl + '/videogames?format=json', {
            method : 'GET'
        })).json()
    },

    async getGameById(id){
        return await (await fetch(baseUrl +'/videogames/' + id + '?format=json', {
            method : 'GET'
        })).json()
    },

    async getRecommendation(id){
        return await(await fetch(baseUrl + '/recommendation/' + id + '?format=json', {
            method : 'GET'
        })).json()
    }
}