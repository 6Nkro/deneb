<template>
  <div id="library">
    <library-side-bar
      :sideBar="sideBar"
      :bookcase="bookcase.bookcase"
      @toggle="sideBar = !sideBar"
      @select="select"/>
    <bookcase-contents
      :sideBar="sideBar"
      :bookcase="bookcase.bookcase"
      :bookcaseIndex="bookcaseIndex"
      @select="select"/>
  </div>
</template>

<script>
import LibrarySideBar from '@/components/library/LibrarySideBar'
import BookcaseContents from '@/components/bookcase/BookcaseContents'

export default {
  components: { LibrarySideBar, BookcaseContents },
  data () {
    return {
      sideBar: true,
      bookcase: this.$store.state.bookStore.library[0],
      bookcaseIndex: 0
    }
  },
  setup () {
  },
  created () {
  },
  mounted () {
  },
  unmounted () {
  },
  methods: {
    select (index) {
      this.bookcase = this.$store.state.bookStore.library[index]
      this.bookcaseIndex = index
    }
  }
}
</script>

<style scoped>
#library {
  position: relative;
  display: inline-flex;
  min-height: 100vh;
  padding-top: 64px;
}
</style>
