<template>
<div>
    <div class="game"  v-for="game in games">
        <SimpleGame  :game="game"/>
    </div>
</div>
</template>

<script>
import AllGames from '@/bus/AllGamesEvent'
import GameById from '@/bus/GameByIdEvent'
import Recommendation from '@/bus/RecommendationEvent'
import GameRequests from '@/services/GameRequests'
import SimpleGame from '@/components/SimpleGame'

export default {
    components: {SimpleGame},
    created () {

        AllGames.$on('games', (opt) => {
            GameRequests.getAllVideoGames(opt.page)
                .then((result) => {
                    this.games = result;
                })
        });

        GameById.$on('id', (args) => {
            GameRequests.getGameById(args)
                .then((result) => {
                    this.games = result;
                })
        });

        Recommendation.$on('id', (args) => {
            GameRequests.getRecommendation(args)
                .then((result) => {
                    this.games = result;
                })
        });

    },
    name: 'GameDisplay',
    data : () => {
        return {
            games : ""
        }
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
    .game{
        display: inline-block;
        margin: 5px;
    }
</style>
