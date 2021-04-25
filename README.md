# image-art-cli

This repo implements Kmeans / Kmedians Clustering with the 
[remisultan/java-ml](https://github.com/remisultan/java-ml) repository in order to produce a 
series of photos per 'K' training.
  
You can end up having a series of image looking like these:

<img src="http://i.freegifmaker.me/1/6/1/9/3/6/16193612803013985.gif" width="200" height="200"> <img src="http://freegifmaker.me/img/res/1/6/1/9/3/6/1619362359631570.gif" width="200" height="200"> <img src="http://freegifmaker.me/img/res/1/6/1/9/3/6/16193624643013935.gif" width="200" height="200"> <img src="http://freegifmaker.me/img/res/1/6/1/9/3/6/161936643763157.gif" width="200" height="200">

## Requirements

- JDK15+
- [remisultan/java-ml](https://github.com/remisultan/java-ml) repository

## Getting started

Clone the repo and execute this

```bash
 $ ./mvnw clean install
 $ nohup bash -x kmedoid.sh MEAN 100 /path/to/file.jpg /path/to/file/prefix 3 5 8 10 20 50 100 200 > /path/to/log/file.log 2>&1 &
 $ tail -f /path/to/log/file.log
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

