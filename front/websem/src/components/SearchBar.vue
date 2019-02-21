<template>
  <div class="search">
    <button v-on:click='allGames()' class="elem">All Games</button>
    <input v-model="input" class="elem"/>
    <button class="elem" v-on:click='byId(input)'>Search By Id</button>
    <button class="elem" v-on:click='recommendation(input)'>Recommendation</button>
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
    return {input: ""}
  },
  methods : {
    allGames : function () {
      AllGames.$emit("games");
    },
    byId : function(id) {
      GameById.$emit("id",id)
    },
    recommendation : function(id){
      Recommendation.$emit("id",id)
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

</style>
