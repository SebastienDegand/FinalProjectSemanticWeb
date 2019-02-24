<template>
    <div>
        <md-card>
            <md-ripple>
                <md-card-header>
                    <div class="md-title">{{ game.name }}</div>
                    <div class="md-subhead">{{ game.id }}</div>
                </md-card-header>

                <md-card-content>
                </md-card-content>

                <md-card-actions>
                    <md-button v-on:click="goToWiki()" >Wikidata</md-button>
                    <md-button v-on:click="getRecom()" >Recommendation</md-button>
                </md-card-actions>
            </md-ripple>
        </md-card>
    </div>
</template>

<script>

    import Recommendation from '@/bus/RecommendationEvent'

    export default {
        name: "SimpleGame",
        props : {
            game : Object
        },
        methods : {
            goToWiki : function () {
                window.open(this.game.url, '_blank');
            },
            getRecom : function(){
                Recommendation.$emit("id",this.game.id)
            }
        }
    }
</script>

<style scoped>

</style>