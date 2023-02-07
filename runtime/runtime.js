const fs = require('fs');
const wat_wasm = require('wat-wasm');

let memory;

function printStrFromMemory(strStart, strSize) {
  const memView = new Uint8Array(memory.buffer, strStart, strSize)

  console.log(String.fromCharCode(...memView));
}

const imports = {
  std: {
    printlnInt: num => console.log(`${num}\n`),
    printlnFloat: num => console.log(`${num}\n`),
    printlnBoolean: num => console.log(`${Boolean(num)}\n`),
    printlnString: (strStart, strSize) => printStrFromMemory(strStart, strSize)
  }
}

async function instantiateWasmFile(imports) {
  const path = process.argv[2];

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
