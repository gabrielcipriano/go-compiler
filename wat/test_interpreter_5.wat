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
  (data(i32.const 0) "contagem regressiva de 10 até 5")
  (data(i32.const 31) "contagem regressiva de 5 até 10 (??)")
  (global $offset (mut i32) (i32.const 67))
  
  (func $countdown (export "countdown") (param $00_start i32) (param $01_end i32) (result i32)    
    (local $02_i i32)
    
    (local.get $01_end)
    (local.get $00_start)
    (i32.gt_s)
    (if
      (then
        (local.get $00_start)
        (call $printlnInt)
        
        (local.get $00_start)
        (return)
      )
    )
    
    (block $BLOCK_0
      (local.get $00_start)
      (local.set $02_i)
      (local.get $02_i)
      (local.get $01_end)
      (i32.ge_s)
      (i32.eqz)
      (br_if $BLOCK_0)
      (loop $FOR_0
        (local.get $02_i)
        (call $printlnInt)
        (local.get $02_i)
        (i32.const 1)
        (i32.sub)
        (local.set $02_i)
        (local.get $02_i)
        (local.get $01_end)
        (i32.ge_s)
        (br_if $FOR_0)
      )
    )
    
    (local.get $01_end)
    (return)
  )
  
  (func $main (export "main") (result i32)    
    (local $03_rocketLaunch i32)
    
    (i32.const 0)
    (i32.const 31)
    (call $printlnString)
    
    (i32.const 10)
    
    (i32.const 5)
    (call $countdown)
    (local.set $03_rocketLaunch)
    
    (i32.const 31)
    (i32.const 36)
    (call $printlnString)
    
    (i32.const 5)
    
    (i32.const 10)
    (call $countdown)
    
    (local.get $03_rocketLaunch)
    (return)
  )
)
