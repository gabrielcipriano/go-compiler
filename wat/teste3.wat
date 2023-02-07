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
  (global $aux_f32 (mut f32) (f32.const 0.000000))
  
  ;; adding strings to memory
  (data(i32.const 0) "\07\00\00\00")
  (data(i32.const 4) "rogerio")
  (data(i32.const 11) "\07\00\00\00")
  (data(i32.const 15) "gabriel")
  (global $offset (mut i32) (i32.const 22))
  
  (func $main (export "main")     
    (local $00_gerim i32)
    (local $01_nome i32)
    (local $02_a f32)
    (local $03_b f32)
    (local $04_a f32)
    (local $05_c f32)
    (local $06_a i32)
    
    (i32.const 0)
    (local.set $00_gerim)
    
    (i32.const 11)
    (local.set $01_nome)
    
    (i32.const 1)
    (i32.const 2)
    (i32.eq)
    (if
      (then
        (i32.const 1)
        (f32.convert_i32_s)
        (local.set $02_a)
        
        (i32.const 2)
        (f32.convert_i32_s)
        (local.set $03_b)
      )
      (else
        (i32.const 0)
        (i32.const 0)
        (i32.or)
        (if
          (then
            (i32.const 1)
            (f32.convert_i32_s)
            (local.set $04_a)
            
            (i32.const 2)
            (f32.convert_i32_s)
            (local.set $05_c)
            
            (f32.const 4.5)
            (local.set $04_a)
          )
          (else
            (i32.const 1)
            (local.set $06_a)
            
            (i32.const 10)
            (local.set $06_a)
            
            (local.get $06_a)
            (call $printlnInt)
          )
        )
      )
    )
    
    (local.get $00_gerim)
    ;; prints string geting the size & moving offset
    (global.set $aux_i32)
    (global.get $aux_i32)
    (i32.const 4)
    (i32.add)
    (global.get $aux_i32)
    (i32.load)
    (call $printlnString)
  )
)