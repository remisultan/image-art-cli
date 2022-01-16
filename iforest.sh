#!/usr/bin/bash

export BACKEND_PRIORITY_CPU=1
export BACKEND_PRIORITY_GPU=0
export OMP_NUM_THREADS=12

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
JAVA_OPTS="$JAVA_OPTS -Djava.library.path=/usr/java/packages/lib:/usr/lib64:/lib64:/lib:/usr/lib:${SCRIPT_DIR}/target/lib/"
JAVA_OPTS="$JAVA_OPTS -DXmx=21G -Dorg.bytedeco.javacpp.maxphysicalbytes=22G --enable-preview"

type=$1
input_file=$2
output_prefix=$3

shift 3

for n in "$@"; do
  echo "$n"
  java $JAVA_OPTS \
      -cp "${SCRIPT_DIR}/target/image-art-cli-0.1.jar:`find target/lib | xargs -I {} echo "$SCRIPT_DIR/{}" | paste -d":" -s`" \
       org.rsultan.app.ImageArtCliCommand iforest -n "${n}" -t ${type} -i "${input_file}" -o "${output_prefix}_${n}.png"
done

