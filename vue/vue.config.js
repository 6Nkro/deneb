const { defineConfig } = require('@vue/cli-service')
const path = require('path')
module.exports = defineConfig({
  transpileDependencies: true,
  publicPath: process.env.NODE_ENV === 'production' ? './' : '',
  outputDir: path.resolve(__dirname, '../src/main/resources/static'),
  css: {
    loaderOptions: {
      sass: {
        additionalData: `
                    @import "@/assets/scss/_reset.scss";
                    @import "@/assets/scss/_variables.scss";
                    @import "@/assets/scss/_breakpoints.scss";
                    @import "@/assets/scss/_mixins.scss";
                `
      }
    }
  }
})
