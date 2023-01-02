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
      <v-icon
        icon="mdi-share"
        size="x-large"/>
    </span>

    <div class="modal__content">

      <v-radio-group
        class="mb-3"
        v-model="onlyCode"
        inline
        hide-details>

        <v-row>
          <v-col cols="6" class="text-end">
            <v-radio
              label="공유 코드 생성"
              :value="true"
              color="indigo"/>
          </v-col>

          <v-col cols="6" class="text-start">
            <v-radio
              label="공개 등록"
              :value="false"
              color="indigo"/>
          </v-col>
        </v-row>

      </v-radio-group>

      <v-expand-transition>
        <v-responsive
          v-show="!onlyCode"
          class="mx-auto"
          max-width="320">
          <v-text-field
            class="mb-7"
            variant="underlined"
            label="이름"
            v-model="bookcase_name"
            :class="{invalid: bookcase_name === ''}"
            :rules="[rules.required]"
            :disabled="onlyCode"
            counter
            maxlength="12"/>

          <v-textarea
            variant="outlined"
            label="내용"
            v-model="share_contents"
            :class="{invalid: share_contents === ''}"
            no-resize
            rows="3"
            :disabled="onlyCode"
            counter
            maxlength="1000"/>

          <v-combobox
            class="input-tag"
            label="tag"
            v-model="temp_tag"
            :items="[]"
            multiple
            :delimiters="[',', ' ']"
            hide-details
            hide-selected
            prepend-inner-icon="mdi-tag"
            @input="moveTag"
            @keydown.enter="moveTag"/>

          <v-slide-group
            class="slide-tag"
            show-arrows>
            <v-slide-group-item
              v-for="tag in tags"
              :key="tag">
              <v-chip
                class="mx-1"
                closable
                @click:close="deleteTag(tag)">
                {{ tag }}
              </v-chip>
            </v-slide-group-item>
          </v-slide-group>

        </v-responsive>
      </v-expand-transition>

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
      onlyCode: true,
      text: '확인',
      loading: false,
      bookcase_name: this.bookcase.bookcase_name,
      share_contents: '',
      temp_tag: [],
      tags: [],
      rules: {
        required: value => !!value || ''
      }
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
    close () {
      this.showModal = true
      this.$emit('close', 'shareBookcase')
    },
    moveTag () {
      if (this.temp_tag.length > 0) {
        const chips = this.temp_tag.pop()
        if (!this.tags.includes(chips)) {
          this.tags.push(chips)
        }
      }
    },
    deleteTag (tag) {
      const index = this.tags.findIndex(item => item === tag)
      this.tags.splice(index, 1)
    },
    async create () {
      const items = document.querySelectorAll('.invalid')
      if (!this.onlyCode && (this.loading || !this.$isValid(items))) {
        return false
      }
      this.text = ''
      this.loading = true
      const url = '/share/create'
      const params = {
        origin_seq: this.bookcase.bookcase_seq,
        account_seq: this.$store.state.accountStore.account.account_seq,
        bookcase_name: this.onlyCode ? this.bookcase.bookcase_name : this.bookcase_name,
        share_public: this.onlyCode ? 'N' : 'Y',
        share_contents: this.onlyCode ? '-' : this.share_contents,
        share_tag: this.onlyCode ? '-' : encodeURIComponent(JSON.stringify(this.tags))
      }
      const res = await this.$axios.post(url, null, { params })
      if (!res.data) {
        return false
      }
      // this.$store.commit('addBook', {
      //   book: res.data,
      //   bookcaseIndex: this.bookcaseIndex
      // })
      this.close()
      const data = {
        icon: 'success',
        html: '<strong>' + res.data.bookcase.share_code + '</strong>' +
          '<br><br>코드는 <a href="/">공유함<a>에서 다시 확인할 수 있어요.'
      }
      this.$swal.fire(data)
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
  transition: 0.3s;
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

.input-tag {
  margin: 1rem 0 0 0;
}

.slide-tag {
  margin: 1rem 0 0 0;
}

button {
  width: 20rem;
  margin: 1rem 0 0 0;
}

</style>
