<template>
    <div>
    <div class="title">{{game.name}}</div>
    <div class="id">{{game.id}}</div>
    </div>
</template>

<script>
    export default {
        name: "SimpleGame",
        props : {
            game : Object
        }
    }
</script>

<style scoped>

</style>