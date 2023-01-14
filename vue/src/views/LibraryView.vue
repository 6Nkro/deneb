<template>
  <div id="circular" v-if="!loadComplete && this.$store.state.accountStore.isLogin">
    <div class="mx-auto text-center">
      <v-progress-circular
        class="mb-5"
        :size="200"
        :width="15"
        color="grey"
        indeterminate/>
      <div>
        <strong style="color: grey; font-size: x-large">데이터를 불러오고 있어요.</strong>
      </div>
    </div>
  </div>
  <div id="library" v-if="loadComplete">
    <library-side-bar
      :sideBar="sideBar"
      :bookcase="bookcase.bookcase"
      @create="createBookcase = true"
      @toggle="sideBar = !sideBar"
      @select="selectBookcase"
    />
    <bookcase-contents
      :sideBar="sideBar"
      :bookcase="bookcase.bookcase"
      :bookcaseIndex="bookcaseIndex"
      @select="selectBookcase"
    />
    <create-bookcase
      v-if="createBookcase"
      @close="createBookcase = false"
      @select="selectBookcase"
    />
  </div>
  <div id="temp" v-if="!this.$store.state.accountStore.isLogin"/>
</template>

<script>
import LibrarySideBar from '@/components/library/sidebar/LibrarySideBar'
import BookcaseContents from '@/components/library/bookcase/BookcaseContents'
import CreateBookcase from '@/components/library/sidebar/CreateBookcase'

export default {
  components: { LibrarySideBar, BookcaseContents, CreateBookcase },
  data () {
    return {
      sideBar: true,
      bookcase: this.$store.state.bookStore.library[0],
      bookcaseIndex: 0,
      createBookcase: false,
      loadComplete: false
    }
  },
  setup () {
  },
  async created () {
    if (!this.$store.state.accountStore.isLogin) {
      this.$emit('setLoginModal', true)
      return false
    }
    const url = '/bookcase/list'
    const params = {
      account_seq: this.$store.state.accountStore.account.account_seq
    }
    const res = await this.$axios.get(url, { params })
    this.$store.commit('refresh', res.data)
    if (this.$route.query.recent !== undefined) {
      this.bookcase = this.$store.state.bookStore.library[res.data.length - 1]
      this.bookcaseIndex = res.data.length - 1
    }
    this.loadComplete = true
  },
  mounted () {
  },
  unmounted () {
  },
  methods: {
    selectBookcase (index) {
      if (index.constructor !== Number) {
        return false
      }
      this.bookcase = this.$store.state.bookStore.library[index]
      this.bookcaseIndex = index
    }
  }
}
</script>

<style scoped>
#circular {
  position: relative;
  display: flex;
  min-height: 100vh;
  align-items: center;
}

#library {
  position: relative;
  display: inline-flex;
  min-height: 100vh;
}

#temp {
  min-height: 100vh;
}
</style>
