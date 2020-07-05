module.exports = {
    publicPath: "/",
    outputDir: "dist",
    lintOnSave: true,
    runtimeCompiler: false,
    productionSourceMap: true,
    chainWebpack: () => {},
    parallel: require("os").cpus().length > 1,
    pwa: {},
    devServer: {
        port: 7001
    },
    pluginOptions: {}
}
