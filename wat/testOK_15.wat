(module 
  ;; Importing std i/o
  (import "std" "printlnInt" (func $printlnInt (param i32)))
  (import "std" "printlnBoolean" (func $printlnBoolean (param i32)))
  (import "std" "printlnFloat" (func $printlnFloat (param f32)))
  (import "std" "printlnString" (func $printlnString (param i32) (param i32)))
  
  ;; creating and exporting memory
  (memory $memory 1)
  (export "memory" (memory $memory))
  
  ;; declaring aux vars
  (global $aux_i32 (mut i32) (i32.const 0))
  (global $aux_f32 (mut f32) (f32.const 0,000000))
  
  ;; adding strings to memory
  (data(i32.const 0) "tela")
  (data(i32.const 4) "Stdin")
  (data(i32.const 9) "Simple Shell")
  (data(i32.const 21) "---------------------")
  (data(i32.const 42) "hello, Yourself")
  (global $offset (mut i32) (i32.const 57))
  
  (func $NewReader (export "NewReader") (param $00_std i32) (result i32)    
    
    (i32.const 0)
    (return)
  )
  
  (func $main (export "main")     
    (local $01_reader i32)
    
    (i32.const 4)
    (call $NewReader)
    (local.set $01_reader)
    
    (i32.const 9)
    (i32.const 12)
    (call $printlnString)
    
    (i32.const 21)
    (i32.const 21)
    (call $printlnString)
    
    (block $BLOCK_0
