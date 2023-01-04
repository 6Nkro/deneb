import { createStore } from 'vuex'
import createPersistedState from 'vuex-persistedstate'

import accountStore from '@/store/modules/accountStore'
import bookStore from '@/store/modules/bookStore'

export default createStore({
  mutations: {
    setLogin: function (state, payload) {
      state.accountStore.isLogin = true
      state.accountStore.account = payload.account
      state.bookStore.library = payload.library
    },
    setLogout: function (state) {
      state.accountStore.isLogin = false
      state.accountStore.account = {
        account_seq: 0,
        user_email: 'guest',
        user_pw: 'guest',
        user_name: 'guest',
        bookcase_order: []
      }
      state.bookStore.library = []
    },
    refresh: function (state, payload) {
      state.bookStore.library = payload
      state.bookStore.library.splice(0, 0)
    },
    addBookcase: function (state, payload) {
      state.bookStore.library.push(payload.bookcase)
    },
    delBookcase: function (state, payload) {
      state.bookStore.library.splice(payload.bookcaseIndex, 1)
    },
    editBookcaseName: function (state, payload) {
      state.bookStore.library[payload.bookcaseIndex].bookcase.bookcase_name = payload.bookcase.bookcase_name
      state.bookStore.library.splice(0, 0)
    },
    editBookcaseOrder: function (state, payload) {
      state.bookStore.library = payload
      state.bookStore.library.splice(0, 0)
    },
    addBook: function (state, payload) {
      state.bookStore.library[payload.bookcaseIndex].bookList.push(payload.book)
    },
    delBook: function (state, payload) {
      const index = state.bookStore.library[payload.bookcaseIndex].bookList
        .findIndex(item => item.book.book_seq === payload.book.book_seq)
      state.bookStore.library[payload.bookcaseIndex].bookList.splice(index, 1)
    },
    editBook: function (state, payload) {
      state.bookStore.library[payload.bookcaseIndex].bookList[payload.bookIndex].book = payload.book
      state.bookStore.library.splice(0, 0)
    },
    moveBook: function (state, payload) {
      state.bookStore.library = payload
      state.bookStore.library.splice(0, 0)
    },
    editBookOrder: function (state, payload) {
      state.bookStore.library[payload.bookcaseIndex].bookList = payload.book
      state.bookStore.library.splice(0, 0)
    },
    editBookState: function (state, payload) {
      state.bookStore.library[payload.bookcaseIndex].bookList[payload.bookIndex].book.book_open = payload.value
    },
    editVideoIndex: function (state, payload) {
      state.bookStore.library[payload.bookcaseIndex].bookList[payload.bookIndex].book.video_index = payload.value
    },
    addBookmark: function (state, payload) {
      state.bookStore.library[payload.bookcaseIndex].bookList[payload.bookIndex].bookmarkList.push(payload.bookmark)
    },
    editBookmarkOrder: function (state, payload) {
      state.bookStore.library[payload.bookcaseIndex].bookList[payload.bookIndex].bookmarkList = payload.bookmarkList
      state.bookStore.library.splice(0, 0)
    },
    moveBookmark: function (state, payload) {
      const index = state.bookStore.library[payload.bookcaseIndex].bookList[payload.bookIndex].bookmarkList
        .findIndex(item => item.bookmark_seq === payload.bookmark.bookmark_seq)
      state.bookStore.library[payload.bookcaseIndex].bookList[payload.bookIndex].bookmarkList[index] = payload.bookmark
    },
    delBookmark: function (state, payload) {
      state.bookStore.library[payload.bookcaseIndex].bookList[payload.bookIndex].bookmarkList.splice(payload.bookmarkIndex, 1)
    },
    editBookmark: function (state, payload) {
      state.bookStore.library[payload.bookcaseIndex].bookList[payload.bookIndex].bookmarkList[payload.bookmarkIndex] = payload.bookmark
      state.bookStore.library.splice(0, 0)
    },
    editVideoTime: function (state, payload) {
      const index = state.bookStore.library[payload.bookcaseIndex].bookList[payload.bookIndex].bookmarkList
        .findIndex(item => item.bookmark_seq === payload.bookmark_seq)
      state.bookStore.library[payload.bookcaseIndex].bookList[payload.bookIndex].bookmarkList[index].video_time = payload.value
    }
  },
  modules: {
    accountStore: accountStore,
    bookStore: bookStore
  },
  plugins: [createPersistedState({
    paths: ['accountStore', 'bookStore']
  })]
})
