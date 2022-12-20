#!/bin/bash
ROOT=/home/rtfsmonteiro/Documents/go-compiler
ANTLR_PATH=$ROOT/tools/antlr-4.11.1-complete.jar
CLASS_PATH_OPTION="-cp .:$ANTLR_PATH"
GRAMMAR_NAME=GO
GEN_PATH=$ROOT/src
GEN_PACKAGE=parser
DATA=$ROOT/tests
IN=$DATA/in_OK
OUT=$DATA/in_OK/out
cd $GEN_PATH
for infile in `ls $IN/*.go`; do
  base=$(basename $infile)
  outfile=$OUT/${base/.go/.out}
  echo Running $base
  # /usr/bin/env /usr/lib/jvm/java-11-openjdk-amd64/bin/java @/tmp/cp_1a318z5kum1cicb8dttgo494v.argfile Main $infile > $outfile
  cat $infile > $outfile
  echo -e '\n\n________________________________________________________________________________\n\n' >> $outfile
  make run FILE="$infile" >> $outfile 2>> $outfile
done
