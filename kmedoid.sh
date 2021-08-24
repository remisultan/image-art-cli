#!/usr/bin/bash

export BACKEND_PRIORITY_CPU=1
export BACKEND_PRIORITY_GPU=0
export OMP_NUM_THREADS=12

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
JAVA_OPTS="$JAVA_OPTS -Djava.library.path=/usr/java/packages/lib:/usr/lib64:/lib64:/lib:/usr/lib:${SCRIPT_DIR}/target/lib/"
JAVA_OPTS="$JAVA_OPTS -DXmx=11G -Dorg.bytedeco.javacpp.maxphysicalbytes=12G --enable-preview"

medoid_type=$1
epochs=$2
input_file=$3
output_prefix=$4

shift 4

for k in "$@"; do
  echo "$k"
  java $JAVA_OPTS \
      -cp "${SCRIPT_DIR}/target/image-art-cli-0.1.jar:`find target/lib | xargs -I {} echo "$SCRIPT_DIR/{}" | paste -d":" -s`" \
       org.rsultan.app.ImageArtCliCommand kmedoid \
      -t "${medoid_type}" \
      -k "${k}" \
      -e "${epochs}" \
      -f -s 0.1 \
      -i "${input_file}" \
      -o "${output_prefix}_${k}_${medoid_type}_${epochs}.png"
done

