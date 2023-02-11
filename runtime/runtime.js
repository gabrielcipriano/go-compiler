const fs = require('fs');

let memory;

function printStrFromMemory(strStart, strSize) {
  const memView = new Uint8Array(memory.buffer, strStart, strSize)

  process.stdout.write(String.fromCharCode(...memView));
}

const imports = {
  std: {
    println: num => process.stdout.write(`\n`),
    printlnInt: num => process.stdout.write(`${num}`),
    printlnFloat: num => process.stdout.write(`${num}`),
    printlnFloat: num => process.stdout.write(`${num}`),
    printlnBoolean: num => process.stdout.write(`${Boolean(num)}`),
    printlnString: (strStart, strSize) => printStrFromMemory(strStart, strSize),
    randInt: num => Math.round(Math.random() * num)
  },
}

require('wabt')()
  .then(wabt => {
    const watFilePath = process.argv[2];

    const filename = watFilePath.split('/').at(-1).split('.')[0];
    const wasmFilePath = `bin/${filename}.wasm`;

    var wasm = wabt.parseWat(watFilePath,
      fs.readFileSync(watFilePath).toString());

    fs.writeFileSync(wasmFilePath,
      wasm.toBinary({ log: true }).buffer);
    
    return wasmFilePath;
  })
  .then((path) => {
    instantiateWasmFile(imports, path)
      .then(async wasmModule => {
        memory = wasmModule.instance.exports.memory;
        const { main } = wasmModule.instance.exports;
        main();
      });
  })
  .catch(err => console.log(err))

async function instantiateWasmFile(imports, path) {

  if (!path)
    throw new Error("Please provide a wasm file as argument");

  const wasmBuffer = fs.readFileSync(path);

  return WebAssembly.instantiate(wasmBuffer, imports);
}

