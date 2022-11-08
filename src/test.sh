#!/bin/bash
ROOT=/home/ryan/Documents/2022-2/Compiladores/go-compiler
ANTLR_PATH=$ROOT/tools/antlr-4.11.1-complete.jar
CLASS_PATH_OPTION="-cp .:$ANTLR_PATH"
GRAMMAR_NAME=GO
GEN_PATH=$ROOT
GEN_PACKAGE=parser
DATA=$ROOT/tests
IN=$DATA/in_OK
OUT=$DATA/out
cd $GEN_PATH
for infile in `ls $IN/*.go`; do
  base=$(basename $infile)
  outfile=$OUT/${base/.go/.out}
  echo Running $base
  /usr/bin/env /usr/lib/jvm/java-11-openjdk-amd64/bin/java @/tmp/cp_1a318z5kum1cicb8dttgo494v.argfile Main $infile > $outfile
  # java $CLASS_PATH_OPTION org.antlr.v4.gui.TestRig $GEN_PACKAGE.$GRAMMAR_NAME tokens -tokens $infile 2>&1 | diff -w $outfile -
done
