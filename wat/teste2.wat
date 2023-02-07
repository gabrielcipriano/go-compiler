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
  
  ;;  adding strings to memory
  (data(i32.const 0) "\07\00\00\00") ;; str size
  (data(i32.const 4) "gabriel")
  (data(i32.const 11) "\07\00\00\00") ;; str size
  (data(i32.const 15) "rogerio")
  (global $offset (mut i32) (i32.const 22))
  
  (global $nome (mut i32) (i32.const 0))
  
  (func $main (export "main")     
    (local $gerim i32)
    (i32.const 11)
    (local.set $gerim)
    
    (local.get $gerim)
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