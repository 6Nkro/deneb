<template>
  <v-btn
  variant="outlined">
    {{ text }}
    <v-progress-circular
      v-if="loading"
      width="3"
      size="20"
      color="blue"
      indeterminate/>
  </v-btn>

</template>

<script>
export default {
  props: {
    text: String,
    loading: Boolean
  }
}
</script>

<style scoped>

button {
  color: #808080;
  background-color: transparent;
  border-radius: 0.8rem;
  padding: 0.5rem 1rem;
  background-size: 200% 100%;
  background-image: linear-gradient(to right, transparent 50%, #808080 50%);
  transition: background-position .3s cubic-bezier(0.19, 1, 0.22, 1) .1s, color .5s ease 0s, background-color .5s ease;
}

button:hover {
  color: white;
  background-color: #808080;
  background-position: -100% 100%;
}

.loading {
  cursor: wait;
}
</style>
