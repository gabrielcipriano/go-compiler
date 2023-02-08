const fs = require('fs');

let memory;

function printStrFromMemory(strStart, strSize) {
  const memView = new Uint8Array(memory.buffer, strStart, strSize)

  console.log(String.fromCharCode(...memView));
}

const imports = {
  std: {
    printlnInt: num => console.log(`${num}`),
    printlnFloat: num => console.log(`${num}`),
    printlnBoolean: num => console.log(`${Boolean(num)}`),
    printlnString: (strStart, strSize) => printStrFromMemory(strStart, strSize)
  }
}

let filename = '';

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

