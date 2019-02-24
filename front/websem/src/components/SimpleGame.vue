<template>
    <div>
        <md-dialog :md-active.sync="dialog">
            <md-dialog-title>{{ detailed.name}}</md-dialog-title>

            <md-dialog-content>
                <div>
                    genre : {{detailed.genres}}
                </div>
                <div>
                    url : {{detailed.url}}
                </div>
                <div>
                    types : {{detailed.types}}
                </div>
                <div>
                    publication date : {{detailed.publication}}
                </div>
                <div>
                   user score : {{detailed.score}}
                </div>
                <div>
                    sells : {{detailed.sells  }}
                </div>
            </md-dialog-content>

            <md-dialog-actions>
                <md-button class="md-primary" v-on:click="dialog = false">Close</md-button>
            </md-dialog-actions>
        </md-dialog>

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
                    <md-button v-on:click="showDialog()">Details</md-button>
                </md-card-actions>
            </md-ripple>
        </md-card>


    </div>
</template>

<script>

    import Recommendation from '@/bus/RecommendationEvent';
    import GameRequests from '@/services/GameRequests'


    export default {
        name: "SimpleGame",
        data: () => {
            return {
                dialog: false,
                detailed : {},
                info : false
            }
        },
        props : {
            game : Object
        },
        methods : {
            goToWiki : function () {
                window.open(this.game.url, '_blank');
            },
            getRecom : function(){
                Recommendation.$emit("id",this.game.id)
            },
            showDialog : async function(){
                if(!this.info){
                    let gameDet = await GameRequests.getGameById(this.game.id);
                    console.log(gameDet);
                    this.detailed = gameDet[0];
                    this.info = true;
                }
                this.dialog = true;

            }

        }
    }
</script>

<style scoped>

</style>