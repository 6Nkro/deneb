<template>
  <navigation-bar></navigation-bar>
  <router-view/>
</template>

<script>
import NavigationBar from '@/components/common/NavigationBar'

export default {
  components: { NavigationBar }
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Gamja+Flower&family=Noto+Sans+KR&family=Roboto&family=Ubuntu&display=swap');

body {
  padding: 0 !important;
}

#app {
  min-width: 100vw;
  box-sizing: border-box;
  font-family: 'Noto Sans KR', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

</style>
