# image-art-cli

This repo implements Kmeans / Kmedians Clustering with the 
[remisultan/java-ml](https://github.com/remisultan/java-ml) repository in order to produce a 
series of photos per 'K' training.
  
You can end up having a series of image looking like these:

![](gifs/me.gif) 
![](gifs/quais.gif) 
![](gifs/spain.gif) 
![](gifs/sky-pan.gif)

## Requirements

- JDK15+
- [remisultan/java-ml](https://github.com/remisultan/java-ml) repository

## Getting started

Clone the repo and execute this

### Kmeans / kMedians
```bash
 $ ./mvnw clean install
 $ bash -x kmedoid.sh MEAN 100 /path/to/file.jpg /path/to/file/prefix 3 5 8 10 20 50 100 200
```

### Principal Component Analysis
```bash
 $ ./mvnw clean install
 $ bash -x pca.sh /path/to/file.jpg /path/to/file/prefix 200 250 300 350 460 512
```

```
usage : bash -x kmedoid.sh \
    [algorithm type: MEAN|MEDIAN] \
    [number of epochs for training] \
    [image to process] \
    [image output prefix] \ 
    [list of 'k' you want to use]
```

Good luck !

