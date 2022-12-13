<template>
  <navigation-bar></navigation-bar>
  <router-view/>
</template>

<script>
import NavigationBar from '@/components/NavigationBar'

export default {
  components: { NavigationBar }
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR&family=Roboto&family=Ubuntu&display=swap');

#app {
  box-sizing: border-box;
  font-family: 'Noto Sans KR', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#app .nav-item a {
  font-family: 'Ubuntu', sans-serif;
  font-size: larger;
  font-weight: bold;
}

.container {
  margin: 5rem 0;
}
</style>
