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
  (data(i32.const 0) "jesus")
  (data(i32.const 5) "maria")
  (data(i32.const 10) "uau")
  (data(i32.const 13) "iupi")
  (data(i32.const 17) "piui")
  (data(i32.const 21) "mãe")
  (global $offset (mut i32) (i32.const 24))
  
  (global $00_a (mut i32) (i32.const 0))
  
  (global $01_b (mut i32) (i32.const 5))
  
  (func $main (export "main")     
    (local $02_sum i32)
    (local $03_div i32)
    (local $04_c i32)
    (local $05_c i32)
    (local $06_c i32)
    (local $07_dilma i32)
    (local $08_d i32)
    
    (i32.const 0)
    (i32.const 1)
    (i32.add)
    (i32.const 4)
    (i32.const 0)
    (i32.const 2)
    (i32.sub)
    (i32.mul)
    (i32.add)
    (local.set $02_sum)
    
    (i32.const 8)
    (i32.const 2)
    (i32.div_s)
    (local.set $03_div)
    
    (i32.const 1)
    (if
      (then
        (i32.const 10)
        (local.set $04_c)
      )
    )
    
    (local.get $02_sum)
    (i32.const 5)
    (i32.gt_s)
    (if
      (then
        (i32.const 13)
        (local.set $05_c)
      )
      (else
        (i32.const 17)
        (local.set $06_c)
      )
    )
    
    (global.get $00_a)
    (i32.const 5)
    (call $printlnString)
    
    (i32.const 21)
    (local.set $07_dilma)
    
    (i32.const 13)
    (local.set $08_d)
  )
)
