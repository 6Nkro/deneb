<template>

  <div class="bookmark-group">
    <div
      v-for="item in bookmarkList"
      :key="item.bookmark_seq"
      class="bookmark d-flex align-center py-1">

      <img
        :src="'https://www.google.com/s2/favicons?domain=' + item.bookmark_url"
        alt="favicon">
      <a
        class="px-1"
        :href="getUrlFormat(item.bookmark_url)"
        target="_blank">{{ item.bookmark_name }}
        <v-tooltip
          activator="parent"
          location="top"
          max-width="360">
          URL : {{ item.bookmark_url }}
          <br><br>
          MEMO : {{ item.bookmark_memo }}
        </v-tooltip>
      </a>

    </div>
  </div>

</template>

<script>

export default {
  props: {
    bookmarkList: Object
  },
  components: {},
  data () {
    return {}
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
    getUrlFormat (url) {
      const isValid = url.startsWith('http://') || url.startsWith('https://')
      return isValid ? url : 'http://' + url
    }
  }
}
</script>

<style scoped>

.bookmark-group {
  padding: 0.5rem
}

.bookmark {
  position: relative;
  white-space: nowrap;
  overflow: hidden;
}
</style>
