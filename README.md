# go-compiler

A Go compiler using ANTLR4 as a frontend

Below is a minimal list of elements that our compiler correctly handle: [specification link](https://drive.google.com/file/d/1Ku9tgrp-aOVd1E94wm8Z764ovLIV9VsK/view)

- Basic arithmetic and comparison operations (+, *, <, ==, etc).
- Assignment statements.
- Execution of sequential code blocks.
- A choice structure (if-else) and the loop structure (for).
- Declaration and manipulation of basic types such as int, float, string, and bool.
- Declaration and manipulation of a composite type (slices).
- Correct declaration and execution of function calls with a fixed number of parameters.
- Type system that handles all permitted types appropriately.
- Basic IO operations on stdin and stdout.

## How to Run

To build the project, simply execute the following command:

```bash
make
```

To perform lexical, syntactic, and semantic validation of a specific test and generate its wat code, execute the following command:

```bash
make run FILE=<file_path>
```

In the case of our tests, the file path is `../tests/<file_name>.go`

To generate the WebAssembly code in readable S form, execute the following command:

```bash
make exec FILE=<file_path>
```

In the case of our tests, the file path is `../wat/<file_name>.go`

However, to generate the wat code in the "wat" folder from the Go code, execute the following command:

```bash
make build-wat FILE=../tests/in_OK/test_wasm_1.go
```

Please note that some file paths and commands may need adjustment depending on your specific project setup.
