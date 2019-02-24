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
    <div class="md-layout md-gutter">
      <div class="md-layout-item">
        <md-field>
          <label >Recommendation Level</label>
          <md-select v-model="lvl" name="lvl" id="lvl" v-on:md-selected="updateReco()">
            <md-option value="1">1</md-option>
            <md-option value="2">2</md-option>
            <md-option value="3">3</md-option>
            <md-option value="0">0</md-option>
          </md-select>
        </md-field>
      </div>

      <div class="md-layout-item">
        <md-field>
          <label>Publisher</label>
          <md-select v-model="publisher" name="publisher" v-on:md-selected="updateReco()" >
            <md-option value="true">True</md-option>
            <md-option value="false">False</md-option>
          </md-select>
        </md-field>
      </div>

      <div class="md-layout-item">
        <md-field>
          <label >Type</label>
          <md-select v-model="type" name="type" v-on:md-selected="updateReco()">
            <md-option value="TripleA">TripleA</md-option>
            <md-option value="IndependantGame">IndependantGame</md-option>
            <md-option value="RetroGame">RetroGame</md-option>
            <md-option value="null">All</md-option>
          </md-select>
        </md-field>
      </div>

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
            page : 0,
    lvl : 0,
    publisher : false,
    type : null}
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
      },
      updateReco : function () {
          this.lvl = parseInt(this.lvl);
          if (this.publisher === "true"){
              this.publisher = true;
          } else if (this.publisher === "false") {
              this.publisher = false;
          }
          if(this.type === "null"){
              this.type = null;
          }
          Recommendation.$emit("config",{lvl : this.lvl , publisher : this.publisher, type : this.type})
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
