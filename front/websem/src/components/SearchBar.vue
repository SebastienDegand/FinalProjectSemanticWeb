<template>
  <div class="search">
    <md-button v-on:click='allGames()' class="elem">Home</md-button>
    <div>
      {{ this.page }}
    </div>
    <div>
      <md-button class="md-raised" v-on:click="previouspage()"><</md-button>
      <md-button class="md-raised" v-on:click="nextpage">></md-button>
    </div>
  </div>
</template>

<script>
  import AllGames from '@/bus/AllGamesEvent'
  import GameById from '@/bus/GameByIdEvent'
  import Recommendation from '@/bus/RecommendationEvent'

export default {
  name: 'SearchBar',
  props: {
    msg: String
  },
  data : () => {
    return {input: "",
            page : 0}
  },
  methods : {
    reset : function(){
        this.page = 0;
        this.allGames();
    },
    allGames : function () {
      AllGames.$emit("games",{page : this.page});
    },
    byId : function(id) {
      GameById.$emit("id",id)
    },
    recommendation : function(id){
      Recommendation.$emit("id",id)
    },
      nextpage : function () {
          this.page++;
          this.allGames()
      },
      previouspage : function(){
        if(this.page > 0) {
            this.page--;
            this.allGames();
        }
      }
  }

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .elem{
    display: inline-block;
    margin-right: 10px;
  }

  md-{
    margin-left: 10px;
    margin-right: 10px;
  }

</style>
