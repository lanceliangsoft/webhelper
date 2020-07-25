const path = require("path");

const base = path.resolve(__dirname, '../..');
const configDir = path.resolve(__dirname);
const source = path.join(base, "@webappDirectory@");
const generatedSource = path.join(base, "target/generatedTs");
const dist = path.join(base, "@distDirectory@");
console.log('basedir=' + base);
console.log('source=' + source);
console.log('generatedSource=' + generatedSource);
console.log('dist=' + dist);

module.exports = {
    context: base,
    mode: "production",
    entry: "@entry@",
    output: {
        path: dist,
        filename: "@outputFilename@"
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                exclude: /node_modules/,
                use: {
                    loader: "ts-loader",
                    options: {
                        transpileOnly: true,
                        configFile: path.join(configDir, "tsconfig.json")
                    }
                }
            },
            {
                test: /\.(png|jpg|jpeg|svg|gif|woff|woff2|ttf|eot)(\?v=\d+\.\d+\.\d+)?$/,
                use: "file-loader"
            },
            {
                test: /\.html$/,
                use: "html-loader"
            },
            {
                test: /\.css$/i,
                use: ["style-loader", "css-loader"]
            }
        ]
    },
    resolve: {
        extensions: ['.tsx', '.ts', '.js', '.jsx', '.css', '.scss', '.less+'],
        modules: [
            "node_modules",
            source,
            generatedSource
        ]
    }
}