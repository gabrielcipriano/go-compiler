(module 
  ;; Importing std i/o
  (import "std" "printlnInt" (func $printlnInt (param i32)))
  (import "std" "printlnBoolean" (func $printlnBoolean (param i32)))
  (import "std" "printlnFloat" (func $printlnFloat (param f32)))
  (import "std" "printlnString" (func $printlnString (param i32) (param i32)))
  (import "random" "randInt" (func $randInt (param i32) (result i32)))
  
  ;; creating and exporting memory
  (memory $memory 1)
  (export "memory" (memory $memory))
  
  ;; declaring aux vars
  (global $aux_i32 (mut i32) (i32.const 0))
  (global $aux_f32 (mut f32) (f32.const 0.000000))
  
  ;; adding strings to memory
  (data(i32.const 0) "Fibonacci sequence:")
  (data(i32.const 19) "Last fibonacci before limit:")
  (global $offset (mut i32) (i32.const 47))
  
  (func $fibonacciIterativo (export "fibonacciIterativo") (param $00_a i32) (param $01_b i32) (param $02_limit i32) (result i32)    
    (local $03_next i32)
  
    (block $BLOCK_0
      (local.get $01_b)
      (local.get $02_limit)
      (i32.le_s)
      (i32.eqz)
      (br_if $BLOCK_0)
      (loop $FOR_0
        (local.get $00_a)
        (call $printlnInt)
        
        (local.get $00_a)
        (local.get $01_b)
        (i32.add)
        (local.set $03_next)
        
        (local.get $01_b)
        (local.set $00_a)
        
        (local.get $03_next)
        (local.set $01_b)
        (local.get $01_b)
        (local.get $02_limit)
        (i32.le_s)
        (br_if $FOR_0)
      )
    )
    
    (local.get $00_a)
    (call $printlnInt)
    
    (local.get $00_a)
    (return)
  )
  
  (func $fibonacciSequence (export "fibonacciSequence") (param $04_limit i32) (result i32)    
    
    (i32.const 0)
    (i32.const 19)
    (call $printlnString)
    
    (i32.const 0)
    
    (i32.const 1)
    
    (local.get $04_limit)
    (call $fibonacciIterativo)
    (return)
  )
  
  (func $main (export "main")     
    (local $05_lastFibonacci i32)
    
    (i32.const 30)
    (call $fibonacciSequence)
    (local.set $05_lastFibonacci)
    
    (i32.const 19)
    (i32.const 28)
    (call $printlnString)
    (local.get $05_lastFibonacci)
    (call $printlnInt)
  )
)