<template>
  <div id="app">
    <SearchBar msg="Welcome to Your Vue.js App"/>
    <GameDisplay/>
  </div>
</template>

<script>
import SearchBar from './components/SearchBar.vue'
import GameDisplay from './components/GameDisplay.vue'

export default {
  name: 'app',
  components: {
    GameDisplay,
    SearchBar
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 10px;
}
</style>
