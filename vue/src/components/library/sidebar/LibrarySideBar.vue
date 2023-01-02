<template>
  <div class="sideBar elevation-3" :class="{hide: !sideBar}">
    <draggable
      tag="div"
      class="bookcase-group"
      v-model="library"
      item-key="bookcase_seq">
      <template #header>
        <div class="sideBar-header elevation-3">
          <span>안녕</span>
          <v-btn
            variant="plain"
            class="toggle"
            :icon="sideBar ? 'mdi-chevron-left' : 'mdi-chevron-right'"
            @click="this.$emit('toggle')">
          </v-btn>
        </div>
      </template>
      <template #item="{ element, index }">
        <v-btn
          block
          class="bookcase"
          :class="{selected: element.bookcase.bookcase_seq === this.bookcase.bookcase_seq}"
          @click="select(index)">
          {{ element.bookcase.bookcase_name }}
        </v-btn>
      </template>
      <template #footer>
        <v-btn
          block
          class="bookcase-create"
          @click="createBookcase = true">
          <v-icon
            icon="mdi-plus-box-multiple"
            color="grey"/>
        </v-btn>
      </template>
    </draggable>
    <create-bookcase v-if="createBookcase" @close="createBookcase = false" @select="select"></create-bookcase>
  </div>
</template>

<script>
import CreateBookcase from '@/components/library/sidebar/CreateBookcase'
import Draggable from 'vuedraggable/src/vuedraggable'

export default {
  props: {
    sideBar: Boolean,
    bookcase: Object
  },
  components: { CreateBookcase, Draggable },
  data () {
    return {
      createBookcase: false
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
  computed: {
    library: {
      get () {
        return this.$store.state.bookStore.library
      },
      set (value) {
        const order = []
        value.forEach(item => {
          order.push(item.bookcase.bookcase_seq)
        })
        const url = '/bookcase/order'
        const params = {
          user_email: this.$store.state.accountStore.account.user_email,
          user_pw: this.$store.state.accountStore.account.user_pw,
          bookcase_order: encodeURIComponent(order)
        }
        this.$axios.post(url, null, { params })
        this.$store.commit('editBookcaseOrder', value)
        const index = this.$store.state.bookStore.library
          .findIndex(item => item.bookcase.bookcase_seq === this.bookcase.bookcase_seq)
        this.$emit('select', index)
      }
    }
  },
  methods: {
    select (index) {
      this.$emit('select', index)
    },
    log (element, list) {
      console.log(element)
      console.log(list)
    }
  }
}
</script>

<style scoped>
.sideBar {
  position: relative;
  width: 20rem;
  min-height: inherit;
  max-height: inherit;
  padding-top: 64px;
  transition: 0.5s;
  background-color: white;
  z-index: 5;
}

.sideBar-header {
  position: relative;
  width: inherit;
  height: 4rem;
  display: flex;
  align-items: center;
  padding-left: 1rem;
  font-size: larger;
  text-align: left;
}

.bookcase-header {
  position: relative;
  width: inherit;
  height: 3rem;
  display: flex;
  align-items: center;
  padding-left: 1rem;
  font-size: large;
  text-align: left;
}

.toggle {
  margin-left: auto;
  width: 4rem;
  height: 4rem;
  font-size: larger;
}

.bookcase {
  white-space: nowrap;
  overflow: hidden;
  width: inherit;
  height: 3rem;
  font-size: large;
  border-radius: 0;
}

.selected {
  background-color: #E3F2FD;
}

.bookcase-create {
  height: 4rem;
  border-radius: 0;
  font-size: x-large;
}

.hide {
  transform: translate(-17rem, 0);
}

button {
  text-transform:none !important;
}

</style>
