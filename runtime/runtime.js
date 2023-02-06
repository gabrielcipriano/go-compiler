const fs = require('fs');

let memory;

function printStrFromMemory(strStart, strSize) {
  const memView = new Uint8Array(memory.buffer, strStart, strSize)

  console.log(String.fromCharCode(...memView));
}

const imports = {
  std: {
    printLnInt: num => console.log(`${num}\n`),
    printLnFloat: num => console.log(`${num}\n`),
    printLnStr: (strStart, strSize) => printStrFromMemory(strStart, strSize)
  }
}

async function instantiateWasmFile(imports) {
  const path = process.argv[2];

  console.info("loading wasm file: " + path);
  if (!path)
    throw new Error("Please provide a wasm file as argument");

  const wasmBuffer = fs.readFileSync(path);

  return WebAssembly.instantiate(wasmBuffer, imports);
}

instantiateWasmFile(imports).then(async wasmModule => {
  memory = wasmModule.instance.exports.memory;
  const { main } = wasmModule.instance.exports;
  main();
});
