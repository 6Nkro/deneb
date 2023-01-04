<template>
  <vue-final-modal
    v-model="showModal"
    classes="modal-container"
    content-class="modal-content elevation-3"
    :lock-scroll="true"
    :click-to-close="true"
    :esc-to-close="true"
    @closed="close">

    <v-icon
      icon="mdi-close"
      class="modal__close"
      color="#808080"
      @click="showModal = false"/>

    <span class="modal__title">
       <input
         type="color"
         class="color-custom"
         v-model="icon_color">
      <v-icon
        :icon="icon"
        size="x-large"
        :color="icon_color"/>
    </span>

    <div class="modal__content">

      <v-radio-group
        class="mb-3"
        v-model="book_type"
        inline
        hide-details>

        <v-row>
          <v-col cols="6" class="text-end">
            <v-radio
              label="북마크 북"
              value="Bookmark"
              color="indigo"/>
          </v-col>

          <v-col cols="6" class="text-start">
            <v-radio
              label="비디오 북"
              value="Video"
              color="indigo"/>
          </v-col>
        </v-row>

      </v-radio-group>

      <div>
        <fieldset>
          <legend>Icon
            <input
              style="position: absolute;"
              type="color"
              v-model="icon_color">
          </legend>

          <v-slide-group
            show-arrows
            style="width:20rem">

            <v-slide-group-item
              v-for="icon in icons"
              :key="icon">
              <v-icon
                class="icon"
                :class="{selected: icon === this.icon}"
                :icon="icon"
                @click="this.icon=icon"/>
            </v-slide-group-item>

          </v-slide-group>

          <div>
            <span
              class="color-filter my-3"
              v-for="item in colors"
              :key="item">
          <div
            class="color-pick"
            :style="{'background-color': item}"
            @click="icon_color=item"/>
            </span>
          </div>
        </fieldset>
      </div>

      <div>
        <fieldset
          :style="`border:2px solid ${book_color}`">
          <legend>Line color
            <input
              style="position: absolute;"
              type="color"
              v-model="book_color">
          </legend>
          <span
            class="color-filter"
            v-for="item in colors"
            :key="item">
          <div
            class="color-pick"
            :style="{'background-color': item}"
            @click="book_color=item"/>
          </span>
        </fieldset>
      </div>

      <v-responsive
        class="mx-auto my-3"
        max-width="320">
        <v-text-field
          variant="underlined"
          label="이름"
          v-model="book_name"
          :class="{invalid: book_name === ''}"
          :rules="[rules.required]"
          counter
          maxlength="12"/>
      </v-responsive>

      <loading-button
        :text="text"
        :loading="loading"
        @click="create"/>
    </div>
  </vue-final-modal>

</template>

<script>
import { VueFinalModal } from 'vue-final-modal'
import LoadingButton from '@/components/common/LoadingButton'

export default {
  props: {
    bookcase: Object,
    bookcaseIndex: Number
  },
  components: {
    VueFinalModal, LoadingButton
  },
  data () {
    return {
      showModal: true,
      text: '생성',
      loading: false,
      book_type: 'Bookmark',
      book_name: '',
      book_color: '#9896A4',
      colors: [
        '#9896A4',
        '#F7C9C9',
        '#F7776A',
        '#DD4132',
        '#F9E03D',
        '#78C752',
        '#97DDDD',
        '#91A8D1',
        '#034F83',
        '#B18F6A'
      ],
      icon: 'mdi-book-multiple',
      icon_color: '#9896A4',
      icons: [
        'mdi-book-open-page-variant',
        'mdi-star',
        'mdi-heart',
        'mdi-book-multiple',
        'mdi-youtube',
        'mdi-email',
        'mdi-forum',
        'mdi-cart',
        'mdi-music',
        'mdi-controller',
        'mdi-silverware-fork-knife',
        'mdi-information',
        'mdi-briefcase',
        'mdi-camera',
        'mdi-movie',
        'mdi-tennis-ball',
        'mdi-map-marker',
        'mdi-car',
        'mdi-headset',
        'mdi-hospital-box',
        'mdi-chef-hat',
        'mdi-cloud',
        'mdi-cog',
        'mdi-dice-3',
        'mdi-glass-cocktail',
        'mdi-lock',
        'mdi-watch'
      ],
      rules: { required: value => !!value || '' }
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
  watch: {
    book_type: function () {
      this.icon = this.book_type === 'Bookmark'
        ? 'mdi-book-multiple'
        : 'mdi-youtube'
    }
  },
  methods: {
    close () {
      this.showModal = true
      this.$emit('close', 'createBook')
    },
    async create () {
      const items = document.querySelectorAll('.invalid')
      if (this.loading || !this.$isValid(items)) {
        return false
      }
      this.text = ''
      this.loading = true
      const url = '/book/create'
      const params = {
        parent_bookcase_seq: this.bookcase.bookcase_seq,
        book_name: this.book_name,
        book_color: this.book_color,
        book_icon: this.icon,
        book_icon_color: this.icon_color,
        book_type: this.book_type
      }
      const res = await this.$axios.post(url, null, { params })
      if (!res.data) {
        return false
      }
      this.$store.commit('addBook', {
        book: res.data,
        bookcaseIndex: this.bookcaseIndex
      })
      this.close()
    }
  }
}
</script>

<style scoped>
::v-deep(.modal-container) {
  display: flex;
  justify-content: center;
  align-items: center;
}

::v-deep(.modal-content) {
  position: relative;
  display: flex;
  flex-direction: column;
  margin: 0 1rem;
  padding: 1rem 0 2rem;
  border: #404040;
  border-radius: 0.25rem;
  background: #fff;
  min-width: 26rem;
  text-align: center;
}

.modal__title {
  position: relative;
  font-size: 1.5rem;
  font-weight: 700;
  margin: 2.5rem 0;
  align-items: center;
}

.modal__close {
  position: absolute;
  top: 1rem;
  right: 1rem;
  cursor: pointer;
  font-size: 1.5rem;
}

fieldset {
  display: inline-block;
  white-space: nowrap;
  width: 20rem;
  margin: 0 0 0.5rem;
  padding: 0.5rem;
  border: 1px solid #C8C8C8;
  border-radius: 0.5rem;
}

legend {
  position: relative;
  color: #808080;
  padding: 0.5rem;
}

legend > input {
  position: absolute;
  top: 0.5rem;
  left: 0;
  width: 100%;
  height: 75%;
  opacity: 0;
  cursor: pointer;
}

.color-pick {
  width: inherit;
  height: inherit;
  border: 1px solid #808080;
}

.color-filter {
  display: inline-block;
  position: relative;
  width: 1.5rem;
  height: 1.5rem;
  margin: 0 4px;
  transition: 0.3s;
  cursor: pointer;
  background-color: white;
  text-align: center;
}

.color-filter:hover {
  opacity: 0.7;
}

.color-filter:active {
  opacity: 1;
}

.color-custom {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 3rem;
  height: 3rem;
  z-index: 3;
  cursor: pointer;
  opacity: 0;
}

.icon:hover {
  opacity: 0.7;
}

.icon:active {
  opacity: 1;
}

.selected {
  opacity: 0.5;
}

.icon {
  width: 1.5rem;
  height: 1.5rem;
  margin: 0 4px;
  transition: 0.3s;
  cursor: pointer;
}

button {
  width: 20rem;
  margin-top: 1rem;
}

</style>
