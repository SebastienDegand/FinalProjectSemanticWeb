<template>
<div>
    {{games}}
</div>
</template>

<script>
    import AllGames from '@/bus/AllGamesEvent'
    import GameById from '@/bus/GameByIdEvent'
    import Recommendation from '@/bus/RecommendationEvent'
    import GameRequests from '@/services/GameRequests'


    export default {
        created () {
            console.log("created");

            AllGames.$on('games', () => {
                GameRequests.getAllVideoGames()
                    .then((result) => {
                        this.games = JSON.stringify(result)
                    })
            });

            GameById.$on('id', (args) => {
                GameRequests.getGameById(args)
                    .then((result) => {
                        this.games = JSON.stringify(result)
                    })
            });

            Recommendation.$on('id', (args) => {
                GameRequests.getRecommendation(args)
                    .then((result) => {
                        this.games = JSON.stringify(result)
                    })
            });

        },
        name: 'Game Display',
        data : () => {
            return {
                games : ""
            }
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
