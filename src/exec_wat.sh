#!/bin/bash
ROOT=/home/ryan/Documents/2022-2/Compiladores/go-compiler
ANTLR_PATH=$ROOT/tools/antlr-4.11.1-complete.jar
CLASS_PATH_OPTION="-cp .:$ANTLR_PATH"
GRAMMAR_NAME=GO
GEN_PATH=$ROOT/src
GEN_PACKAGE=parser
DATA=$ROOT/wat
IN=$DATA
cd $GEN_PATH
for infile in `ls $IN/*.wat`; do
  base=$(basename $infile)
  echo Running $base
  # /usr/bin/env /usr/lib/jvm/java-11-openjdk-amd64/bin/java @/tmp/cp_1a318z5kum1cicb8dttgo494v.argfile Main $infile > $outfile
  # cat $infile > $outfile
  # echo -e '\n\n________________________________________________________________________________\n\n' >> $outfile
  make exec FILE="$infile"
done
