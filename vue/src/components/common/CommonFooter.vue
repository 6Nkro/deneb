<template>
  <div class="pt-1 bg-grey footer">

    <v-btn
      v-for="icon in icons"
      :key="icon"
      class="mx-4"
      :icon="icon"
      variant="plain"
      size="small"/>
  </div>

  <div class="pb-2 bg-grey text-center w-100">
    Copyright © 2023. deneb. All rights reserved.
  </div>
</template>

<script>
export default {
  components: {},
  data () {
    return {
      icons: [
        'mdi-facebook',
        'mdi-twitter',
        'mdi-linkedin',
        'mdi-instagram'
      ]
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
  methods: {}
}
</script>

<style scoped>

.footer {
  width:100%;
  display: inline-flex;
  justify-content: center;
  align-items: center;
  position: relative;
  z-index: 5;
}

button {
  width: 2rem !important;
  height: 2rem !important;
}
</style>
