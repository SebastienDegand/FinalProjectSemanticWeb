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
            GameRequests.getRecommendation(args,this.recoConfig)
                .then((result) => {
                    this.games = result;
                })
        });

        Recommendation.$on('config', (args) => {
            console.log("new config");
            console.log(args);
            this.recoConfig = args
        });

    },
    name: 'GameDisplay',
    data : () => {
        return {
            games : "",
            recoConfig : {lvl: 0, publisher: false, type: null}
        }
    },
    mounted : function() {
        GameRequests.getAllVideoGames(0)
            .then((result) => {
                this.games = result;
            })
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
